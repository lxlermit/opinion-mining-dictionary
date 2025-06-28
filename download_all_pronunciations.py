#!/usr/bin/python

import logging
import os
import json
import time
import random
import requests
from seleniumwire import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from fake_useragent import UserAgent

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    filename='pronunciation_downloader.log',
    filemode='a'
)


def get_existing_audio_files(audio_folder='html/audio'):
    """Получает список уже существующих аудиофайлов"""
    existing_files = set()
    if os.path.exists(audio_folder):
        for filename in os.listdir(audio_folder):
            if filename.endswith('.mp3'):
                existing_files.add(filename[:-4])  # Удаляем расширение .mp3
    return existing_files


def extract_words_from_user_jsons(users_folder='users'):
    """Извлекает английские слова из JSON-файлов пользователей"""
    words = set()
    existing_audio = get_existing_audio_files()

    if not os.path.exists(users_folder):
        logging.error(f"Папка пользователей '{users_folder}' не найдена!")
        return words

    for filename in os.listdir(users_folder):
        if filename.endswith('.json'):
            filepath = os.path.join(users_folder, filename)
            try:
                with open(filepath, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    if 'dict' in data:
                        for word in data['dict'].keys():
                            if word not in existing_audio:
                                words.add(word)
            except Exception as e:
                logging.error(f"Ошибка при чтении файла {filename}: {e}")

    logging.info(f"Найдено {len(words)} уникальных английских слов без существующих аудиофайлов")
    return words


def get_pronunciation(word, save_directory='html/audio'):
    """Функция для получения аудио произношения слова"""
    save_path = os.path.join(save_directory, f"{word}.mp3")
    if os.path.exists(save_path):
        logging.info(f"Аудио для слова '{word}' уже существует в {save_path}")
        return 'exists'

    try:
        # Создаем папку для аудио, если ее нет
        os.makedirs(save_directory, exist_ok=True)

        # Настройка браузера
        ua = UserAgent()
        mobile_user_agents = [
            "Mozilla/5.0 (Linux; Android 10; SM-G960U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
        ]
        user_agent = random.choice(mobile_user_agents)

        chrome_options = Options()
        chrome_options.add_argument('--headless')
        chrome_options.add_argument('--disable-gpu')
        chrome_options.add_argument('--no-sandbox')
        chrome_options.add_argument('--disable-dev-shm-usage')
        chrome_options.add_argument(f'--user-agent={user_agent}')
        chrome_options.add_argument('--disable-blink-features=AutomationControlled')
        chrome_options.add_experimental_option("excludeSwitches", ["enable-automation"])
        chrome_options.add_experimental_option('useAutomationExtension', False)

        with webdriver.Chrome(options=chrome_options) as driver:
            url = f"https://youdao.com/result?word={word}&lang=en"
            driver.get(url)
            time.sleep(3)

            audio_element = driver.find_element(By.TAG_NAME, "audio")
            audio_src = audio_element.get_attribute("src")
            logging.info(f"Найдена прямая ссылка на аудио: {audio_src}")
            print(f"Найдено аудио для слова: {word}")

            response = requests.get(audio_src)
            if response.status_code == 200:
                with open(save_path, "wb") as file:
                    file.write(response.content)
                logging.info(f"Аудио для слова '{word}' сохранено в {save_path}")
                return 'success'
            else:
                logging.error(f"Ошибка при загрузке аудио для слова {word}")
                return 'failed'

    except Exception as e:
        logging.error(f"Произошла ошибка при обработке слова {word}: {e}")
        return 'error'


def download_all_pronunciations():
    """Скачивает произношения для всех новых слов из JSON"""
    words = extract_words_from_user_jsons()
    if not words:
        print("Нет новых слов для скачивания или не найдены JSON-файлы")
        return

    words_list = list(words)
    success_words = []
    failed_words = []
    existing_words = []
    error_words = []

    print(f"\nНачинаем скачивание произношений для {len(words_list)} слов...")
    print("=============================================")

    for i, word in enumerate(words_list):
        result = get_pronunciation(word)

        if result == 'exists':
            existing_words.append(word)
        elif result == 'success':
            success_words.append(word)
        elif result == 'failed':
            failed_words.append(word)
        else:
            error_words.append(word)

        # Добавляем паузы между запросами
        if i < len(words_list) - 1 and result not in ['exists', 'error']:
            sleep_time = random.uniform(3, 10)
            time.sleep(sleep_time)

        if (i + 1) % 10 == 0 and i < len(words_list) - 1 and result not in ['exists', 'error']:
            time.sleep(60)

    # Выводим итоговый отчет
    print("\nРезультаты скачивания:")
    print("=============================================")
    print(f"Успешно скачано: {len(success_words)} слов")
    if success_words:
        print("Список успешных загрузок:", ', '.join(success_words))

    print(f"\nУже существовали: {len(existing_words)} слов")
    if existing_words:
        print("Список существующих файлов:", ', '.join(existing_words))

    print(f"\nНе удалось скачать: {len(failed_words)} слов")
    if failed_words:
        print("Список неудачных загрузок:", ', '.join(failed_words))

    print(f"\nОшибки при обработке: {len(error_words)} слов")
    if error_words:
        print("Список слов с ошибками:", ', '.join(error_words))

    print("\nРабота скрипта завершена!")
    logging.info(
        f"Итоги: успешно - {len(success_words)}, существовали - {len(existing_words)}, неудачно - {len(failed_words)}, ошибки - {len(error_words)}")


if __name__ == "__main__":
    download_all_pronunciations()