from flask import Flask, render_template, request, redirect, url_for, session, send_from_directory, Blueprint
import os
import json
import random
from datetime import datetime

app = Flask(__name__, template_folder='../html', static_folder='../html/audio', static_url_path='/audio')
dictionary_bp = Blueprint('dictionary', __name__, url_prefix='/dictionary')
app.secret_key = 'your_secret_key_here'



def load_user_data(username):
    if username == 'noname':
        filepath = os.path.join('users', 'noname.json')
    else:
        filepath = os.path.join('users', f'{username}.json')

    if not os.path.exists(filepath):
        return None

    with open(filepath, 'r', encoding='utf-8') as file:
        return json.load(file)


def save_user_data(username, data):
    filepath = os.path.join('users', f'{username}.json')
    with open(filepath, 'w', encoding='utf-8') as file:
        json.dump(data, file, indent=4, ensure_ascii=False)


def get_random_word_and_translations(user_data, difficulty):
    words = list(user_data['dict'].items())
    if not words:
        return None, None, []

    random_word, (translation, *_) = random.choice(words)

    if difficulty == 0:  # Легкий (англ -> рус)
        target = random_word
        correct_answer = translation
        all_options = [item[0] for item in user_data['dict'].values()]
    else:  # Средний и Эксперт (рус -> англ)
        target = translation
        correct_answer = random_word
        all_options = list(user_data['dict'].keys())

    all_options.remove(correct_answer)
    wrong_options = random.sample(all_options, min(4, len(all_options)))
    options = wrong_options + [correct_answer]
    random.shuffle(options)

    return target, correct_answer, options


def init_session():
    if 'level_data' not in session:
        session['level_data'] = {
            '0': {'word': None, 'answer': None, 'options': None},
            '1': {'word': None, 'answer': None, 'options': None},
            '2': {'word': None, 'answer': None, 'options': None}
        }
    if 'difficulty' not in session:
        session['difficulty'] = 0


@dictionary_bp.route('/')
def home():
    if 'username' not in session:
        return redirect(url_for('dictionary.login'))

    init_session()
    username = session['username']

    user_data = load_user_data(username)
    difficulty = str(session['difficulty'])

    # Получаем текущее сохраненное слово для этого уровня сложности
    level_data = session['level_data'][difficulty]

    # Если слова нет или запрошено новое слово, генерируем его
    if level_data['word'] is None or request.args.get('new_word'):
        target_word, correct_answer, options = get_random_word_and_translations(user_data, int(difficulty))
        session['level_data'][difficulty] = {
            'word': target_word,
            'answer': correct_answer,
            'options': options
        }
        session.modified = True  # Важно явно указать изменение сессии
    else:
        # Используем сохраненное слово
        target_word = level_data['word']
        correct_answer = level_data['answer']
        options = level_data['options']

    # Остальной код остается без изменений
    today = datetime.now().strftime('%Y-%m-%d')
    if today not in user_data['daily_activity']:
        user_data['daily_activity'][today] = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]

    correct, incorrect, progress = user_data['daily_activity'][today][int(difficulty)]
    daily_goals = user_data.get('daily_goal', [10, 7, 5])
    current_goal = daily_goals[int(difficulty)]
    progress_percentage = (progress / current_goal) * 100 if current_goal > 0 else 0

    daily_progress = [
        user_data['daily_activity'][today][0][2],
        user_data['daily_activity'][today][1][2],
        user_data['daily_activity'][today][2][2]
    ]

    celebration = progress == current_goal
    result = session.pop('result', None)

    return render_template(
        'index.html',
        username=username,
        target_word=target_word,
        correct_answer=correct_answer,
        options=options,
        random_word=correct_answer if difficulty != '0' else target_word,
        result=result,
        progress=progress,
        daily_goal=current_goal,
        daily_goals=daily_goals,
        daily_progress=daily_progress,
        progress_percentage=progress_percentage,
        celebration=celebration,
        current_difficulty=int(difficulty)
    )


@dictionary_bp.route('/change_difficulty/<int:level>')
def change_difficulty(level):
    if 'username' not in session:
        return redirect(url_for('dictionary.login'))

    init_session()
    if level in [0, 1, 2]:
        session['difficulty'] = level
        session.modified = True  # Важно явно указать изменение сессии

    return redirect(url_for('dictionary.home'))


@dictionary_bp.route('/check_answer', methods=['POST'])
def check_answer():
    if 'username' not in session:
        return redirect(url_for('dictionary.login'))

    init_session()
    username = session['username']
    user_data = load_user_data(username)
    difficulty = int(session['difficulty'])
    level_key = str(difficulty)

    if difficulty == 2:  # Режим эксперта
        user_input = request.form['user_input'].strip().lower()
        russian_word = request.form['word']

        correct_english = None
        for eng, (trans, correct, incorrect) in user_data['dict'].items():
            if trans == russian_word:
                correct_english = eng
                break

        is_correct = correct_english and user_input == correct_english.lower()

        if correct_english:
            if is_correct:
                user_data['dict'][correct_english][1] += 1
            else:
                user_data['dict'][correct_english][2] += 1

        result = {
            'correct': is_correct,
            'word': russian_word,
            'correct_answer': correct_english,
            'user_input': user_input,
            'mode': 'expert'
        }
    else:
        selected_answer = request.form['translation']
        word = request.form['word']
        selected_word = None  # Инициализируем переменную заранее

        if difficulty == 0:  # Легкий режим (англ -> рус)
            correct_translation = user_data['dict'][word][0]
            is_correct = selected_answer == correct_translation

            # Находим слово, соответствующее выбранному переводу (даже при правильном ответе)
            selected_word = next((eng for eng, (trans, *_) in user_data['dict'].items()
                                  if trans == selected_answer), None)

            if is_correct:
                user_data['dict'][word][1] += 1
            else:
                user_data['dict'][word][2] += 1
                if selected_word:
                    user_data['dict'][selected_word][2] += 1

            result = {
                'correct': is_correct,
                'word': word,
                'correct_answer': correct_translation,
                'selected_translation': selected_answer,
                'selected_word': selected_word,
                'mode': 'easy'
            }
        else:  # Средний режим (рус -> англ)
            correct_english = None
            for eng, (trans, correct, incorrect) in user_data['dict'].items():
                if trans == word:
                    correct_english = eng
                    break

            is_correct = selected_answer == correct_english

            if correct_english:
                if is_correct:
                    user_data['dict'][correct_english][1] += 1
                else:
                    user_data['dict'][correct_english][2] += 1
                    if selected_answer in user_data['dict']:
                        user_data['dict'][selected_answer][2] += 1

            selected_translation = user_data['dict'].get(selected_answer, [''])[0]

            result = {
                'correct': is_correct,
                'word': word,
                'correct_answer': correct_english,
                'selected_translation': selected_answer,
                'selected_word': selected_translation,
                'mode': 'medium'
            }

    # Остальной код без изменений
    today = datetime.now().strftime('%Y-%m-%d')
    if today not in user_data['daily_activity']:
        user_data['daily_activity'][today] = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]

    if result['correct']:
        user_data['daily_activity'][today][difficulty][0] += 1
        user_data['daily_activity'][today][difficulty][2] += 1
    else:
        user_data['daily_activity'][today][difficulty][1] += 1
        progress = user_data['daily_activity'][today][difficulty][2]
        if progress > 0:
            user_data['daily_activity'][today][difficulty][2] -= 1

    session['level_data'][level_key] = {
        'word': None,
        'answer': None,
        'options': None
    }

    save_user_data(username, user_data)
    session['result'] = result
    return redirect(url_for('dictionary.home'))


@dictionary_bp.route('/audio/<path:filename>')
def serve_audio(filename):
    return send_from_directory(app.static_folder, filename, mimetype='audio/mpeg')


@dictionary_bp.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username'].lower()
        password = request.form['password']

        if not os.path.exists('registration.json'):
            return "Пользователь не найден."

        with open('registration.json', 'r', encoding='utf-8') as file:
            users = json.load(file)

        if username in users and users[username] == password:  # Прямое сравнение пароля
            session['username'] = username
            return redirect(url_for('dictionary.home'))
        else:
            return "Неверный логин или пароль."

    return render_template('login.html')


@dictionary_bp.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        if not os.path.exists('registration.json'):
            with open('registration.json', 'w', encoding='utf-8') as file:
                json.dump({}, file, ensure_ascii=False, indent=4)

        with open('registration.json', 'r', encoding='utf-8') as file:
            users = json.load(file)

        if username in users:
            return "Имя пользователя уже занято."

        users[username] = password  # Сохраняем пароль в открытом виде

        with open('registration.json', 'w', encoding='utf-8') as file:
            json.dump(users, file, ensure_ascii=False, indent=4)

        # Создаем файл пользователя на основе noname.json
        noname_file = os.path.join('users', 'noname.json')
        with open(noname_file, 'r', encoding='utf-8') as file:
            noname_data = json.load(file)

        new_user_file = os.path.join('users', f'{username}.json')
        with open(new_user_file, 'w', encoding='utf-8') as file:
            json.dump(noname_data, file, ensure_ascii=False, indent=4)

        return redirect(url_for('dictionary.login'))

    return render_template('register.html')


@dictionary_bp.route('/edit_dictionary')
def edit_dictionary():
    if 'username' not in session:
        return redirect(url_for('dictionary.login'))

    username = session['username']
    user_data = load_user_data(username)

    return render_template(
        'edit_dictionary.html',
        username=username,
        words=user_data['dict'],
        daily_goal=user_data.get('daily_goal', [10, 7, 5])
    )


@dictionary_bp.route('/add_word', methods=['POST'])
def add_word():
    if 'username' not in session:
        return redirect(url_for('login'))

    username = session['username']
    user_data = load_user_data(username)

    word = request.form['word'].strip().lower()
    translation = request.form['translation'].strip()

    if word not in user_data['dict']:
        user_data['dict'][word] = [translation, 0, 0]
        save_user_data(username, user_data)

    return redirect(url_for('dictionary.edit_dictionary'))


@dictionary_bp.route('/delete_word', methods=['POST'])
def delete_word():
    if 'username' not in session:
        return redirect(url_for('dictionary.login'))

    username = session['username']
    user_data = load_user_data(username)

    word = request.form['word']
    if word in user_data['dict']:
        del user_data['dict'][word]
        save_user_data(username, user_data)

    return redirect(url_for('dictionary.edit_dictionary'))


@dictionary_bp.route('/logout')
def logout():
    session.pop('username', None)
    return redirect(url_for('dictionary.login'))

app.register_blueprint(dictionary_bp)

if __name__ == "__main__":
    app.run(debug=True)