import http from 'k6/http';
import { check, sleep } from 'k6';

// Конфигурация теста
export const options = {
    stages: [
        // Плавно увеличиваем нагрузку до 100 пользователей за 1 минуту
        { duration: '1m', target: 500 },
        // Держим нагрузку 100 пользователей 2 минуты
        { duration: '2m', target: 1000 },
        // Плавно снижаем нагрузку до 0 за 30 секунд
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<2000'], // 95% запросов должны быть быстрее 2 секунд
        http_req_failed: ['rate<0.01'],    // Меньше 1% ошибок
    },
};

export default function () {
    // Генерируем уникальные данные для каждого запроса
    const timestamp = Date.now();
    const randomSuffix = Math.random().toString(36).substring(2, 8);

    const payload = JSON.stringify({
        username: `user_${timestamp}_${randomSuffix}`,
        phone: `+7916${Math.floor(1000000 + Math.random() * 9000000)}`,
        password: 'TestPassword123!'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // Отправляем запрос на регистрацию
    const response = http.post('http://localhost:8080/api/register', payload, params);

    // Проверяем успешность запроса
    check(response, {
        'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
        'response time acceptable': (r) => r.timings.duration < 5000,
    });

    // Пауза между запросами (1-3 секунды)
    sleep(0,5);
}