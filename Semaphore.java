import java.util.concurrent.TimeUnit;
public class Semaphore {
    protected int counter;
    public Semaphore(int initVal){
        if (initVal<0){
            throw new IllegalArgumentException("NO Negative initial semaphore value");
        }
        counter=initVal;
    }
    public synchronized void acquire() throws InterruptedException{
        while (counter==0){
            wait();
        }
        counter--;
    }
    public synchronized boolean tryAcquire(long timeoutMillis) throws InterruptedException{
        if (timeoutMillis<0) timeoutMillis=0;
        long deadline =System.nanoTime()+TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        while (counter==0){
            long remainingNanos = deadline-System.nanoTime();
            if (remainingNanos<=0) return false;

            long millis =TimeUnit.NANOSECONDS.toMillis(remainingNanos);
            int nanos=(int)(remainingNanos - TimeUnit.MILLISECONDS.toNanos(millis));
            wait(millis,nanos);
        }
        counter--;
        return true;

    }
    public synchronized void release(){
        counter++;
        notifyAll();
    }
    public synchronized int availPermits(){
        return counter;
    }
}
