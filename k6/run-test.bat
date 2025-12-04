@echo off
echo 🚀 Запуск нагрузочного тестирования регистрации пользователей...
k6 run register-test.js
pause