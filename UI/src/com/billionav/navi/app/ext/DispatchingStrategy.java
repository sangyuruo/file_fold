package com.billionav.navi.app.ext;

public interface DispatchingStrategy<T> {
	public void dispatch(T message);

	public void handleDispatchingError(String info, Exception ex);

	public void handleQueueingError(String info, Exception ex);
}
