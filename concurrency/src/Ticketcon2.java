import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
class Ticketcon2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(7); // create a thread pool with 7 threads
        AtomicInteger ticketsAvailable = new AtomicInteger(6); // use an atomic integer to avoid race conditions
        Lock lock = new ReentrantLock(); // create a lock to synchronize access to the shared data

        // create seven Runnable objects for booking tickets
        for (int i = 1; i <= 7; i++) {
            final int threadId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        // acquire the lock
                        lock.lock();

                        // make each Runnable wait for 0.5 seconds
                        Thread.sleep(500);

                        // book a ticket
                        int ticketsLeft = ticketsAvailable.decrementAndGet(); // atomically decrement the tickets available and get the new value
                        if (ticketsLeft >= 0) {
                            System.out.println("Thread " + threadId + " booked a ticket. Tickets left: " + ticketsLeft);
                        } else {
                            System.out.println("Thread " + threadId + " could not book a ticket. No tickets left.");
                        }

                        // release the lock
                        lock.unlock();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(task); // execute the Runnable object in the thread pool
        }

        executor.shutdown(); // shut down the thread pool after all tasks are done

    }
}
