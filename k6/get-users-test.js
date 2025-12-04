import http from 'k6/http';
import { check, sleep } from 'k6';

// Конфигурация теста
export const options = {
    stages: [
        // Плавно увеличиваем нагрузку до 100 пользователей за 1 минуту
        { duration: '1m', target: 100 },
        { duration: '1m', target: 300},
        // Держим нагрузку 100 пользователей 2 минуты
        { duration: '1m', target: 500 },
        // Плавно снижаем нагрузку до 0 за 30 секунд
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% запросов должны быть быстрее 500ms
        http_req_failed: ['rate<0.01'],   // менее 1% ошибок
    },
};

// Базовый URL вашего приложения
const BASE_URL = 'http://localhost:8082'; // измените на ваш URL

export default function () {
    // Генерируем случайный ID в диапазоне 1 - 3047374
    const randomUserId = Math.floor(Math.random() * 70000) + 1;
    const url = `${BASE_URL}/profile/${randomUserId}`; // измените путь на ваш

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        tags: { name: 'getProfile' },
    };

    // Выполняем GET запрос
    const response = http.get(url, params);

    // Проверяем результаты
    check(response, {
        'status is 200, 404 or 500': (r) => [200, 404, 500].includes(r.status),
        'response time < 2s': (r) => r.timings.duration < 2000,
    });

    // Дополнительные проверки в зависимости от статуса
    if (response.status === 200) {
        check(response, {
            'successful response has profile data': (r) => {
                try {
                    const body = r.json();
                    return body.userId !== undefined;
                } catch (e) {
                    return false;
                }
            },
        });
    } else if (response.status === 404) {
        check(response, {
            '404 returns user not found message': (r) => {
                const body = r.body;
                return body.includes('not found') || body.includes('User');
            },
        });
    }

    // Короткая пауза между запросами
    sleep(0,5);
}