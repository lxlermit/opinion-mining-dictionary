#!/usr/bin/python
from py.app import app

# def print_routes_with_prefix():
#     print("\n * Available routes:")
#     for rule in app.url_map.iter_rules():
#         if "GET" in rule.methods:
#             print(f" * http://127.0.0.1:5005{rule}")


if __name__ == "__main__":

    # with app.app_context():
    #     print_routes_with_prefix()

    app.run(host='192.168.0.104', port=5000, debug=False)
    # app.run(host='0.0.0.0', port=5005, debug=True)

