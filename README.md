# job4j_cinema

Приложение покупки билетов в кинотеатр
1. Главная страница форма с выбором фильма. Ряда и места. 
Три списка. Если места заняты, то их в списках не показываем. 

2. После загрузки формы отобразить результат покупки. Важно. Пользователь может не купить билет, потому что его купил другой пользователь. То есть одновременно выбрали одинаковые места.

3. Модели. User. Session (Сеансы). Ticket (Купленный билет).

Вычисление свободных мест для сеанса необходимо сделать в слое SessionService. Вычисления делаем по купленным билетам.