package telran.multithreading;

// Deadlock (взаимная блокировка) — это ситуация, когда два или более потока блокируют друг друга,
// потому что каждый из них ждёт, пока другой освободит ресурс.
// В результате ни один поток не может продолжить выполнение.
public class DeadlockExample {

    private static final Object resource1 = new Object(); // Первый ресурс
    private static final Object resource2 = new Object(); // Второй ресурс

    public static void main(String[] args) {
        // Создаём поток номер 1
        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                // thread1 захватывает контроль над первым ресурсом.
                // Более точно - он захватывает монитор объекта resource1. Это значит, что никакой
                // другой поток не сможет войти в synchronized блоки с resource1 до тех пор,
                // пока thread1 не завершит работу и не выйдет из блока

                try {
                    Thread.sleep(100);
                    // Пауза для имитации работы
                } catch (InterruptedException e) {
                }

                // thread1 пытается захватить контроль над вторым ресурсом
                synchronized (resource2) {
                    // Тут он его как бы должен захватить, но до этого не дойдёт, так как
                    // resource2 сейчас занят потоком thread2, который в свою очередь ждёт
                    // освобождения resourse1, который сейчас занят потоком thread1, которвый в свою очередь...
                }
            }
        });

        // Создаём поток номер 2
        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                // thread2 захватывает контроль над вторым ресурсом. Всё то же, что и в первом случае.

                try {
                    Thread.sleep(0);
                    // Пауза для имитации работы
                    // Но она тут и не нужна, поэтому 0
                } catch (InterruptedException e) {
                }

                // thread2 пытается захватить контроль над первым ресурсом
                synchronized (resource1) {
                    // Здесь та же самая ситуация, только отражённая зеркально.
                    // И выйти из вечного ожидания очереди нельзя.
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
