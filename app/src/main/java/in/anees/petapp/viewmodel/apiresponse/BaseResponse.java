package in.anees.petapp.viewmodel.apiresponse;

/**
 * Created by Anees Thyrantakath on 2019-08-15.
 */
public class BaseResponse<T> {

    public enum State {
        LOADING,
        SUCCESS,
        FAILED
    }

    protected T data;
    protected String error;
    protected State currentState;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
