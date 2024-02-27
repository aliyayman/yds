import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.jetbrains.annotations.Contract
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    @Contract(pure = true)
    fun SingleLiveEvent(value: T): SingleLiveEvent<T> {
        this.value = value
        return this
    }

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // Observe the internal MutableLiveData
        super.observe(owner, object : Observer<T> {
            override fun onChanged(@Nullable t: T) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            }
        })
    }


    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        setValue(null)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}