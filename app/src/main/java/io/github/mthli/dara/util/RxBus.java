package io.github.mthli.dara.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private static final RxBus sRxBus = new RxBus();

    public static RxBus getInstance() {
        return sRxBus;
    }

    private Subject<Object, Object> mSubject = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {}

    public void post(Object object) {
        mSubject.onNext(object);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

    public boolean hasObservers() {
        return mSubject.hasObservers();
    }
}
