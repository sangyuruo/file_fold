package com.billionav.navi.gps;

public abstract class Timer {
	/*-----------------------Timer------------------------------*/
	private int 		mmTime = 0;
	private TimerRun 	mmTimerRun = null;
	private boolean 	mmbNotify = false;

	private static final int STATE_STOP 		= 0;
	private static final int STATE_RUN 			= 1;
	private static final int STATE_ON_TIMER 	= 2;

	private int mmState = STATE_STOP;

	/*
	 * Create a timer with time
	 */
	public Timer(int iTime) {
		mmTime = iTime;
	}

	/*
	 * Start the timer
	 */
	public synchronized void startTimer() {

		switch (mmState) {
		case STATE_STOP:
			// Start
			mmTimerRun = new TimerRun();
			mmTimerRun.start();
			break;
		case STATE_RUN:
			// Wake up the thread
			synchronized (mmTimerRun) {
				mmbNotify = true;
				mmTimerRun.notify();
			}
			break;
		default:
			// Nothing to do
			break;
		}

		// Set state to run
		mmState = STATE_RUN;
	}

	/*
	 * Stop the timer
	 */
	public synchronized void stopTimer() {

		switch (mmState) {
		case STATE_RUN:
			mmTimerRun.cancel();
			synchronized (mmTimerRun) {
				mmbNotify = true;
				mmTimerRun.notify();
			}
			break;
		case STATE_ON_TIMER:
			mmTimerRun.cancel();
			break;
		default:
			// Nothing to do
			break;
		}

		// Set state to stop
		mmState = STATE_STOP;
	}

	/*
	 * On timer to do
	 */
	public abstract void onTimer();

	/*
	 * Check is on timer or not
	 */
	public synchronized boolean isOnTimer() {
		return mmState == STATE_ON_TIMER ? true : false;
	}

	private class TimerRun extends Thread {

		private boolean mmmbQuit = false;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {

			// Check quit
			if (mmmbQuit) {
				return;
			}

			while (true) {

				// Check quit
				if (mmmbQuit) {
					return;
				}

				synchronized (this) {

					mmbNotify = false;

					try {
						// Wait for time
						wait(mmTime);

						// Check notify flag
						if (!mmbNotify) {

							// Call back on timer
							// Set state on timer
							mmState = STATE_ON_TIMER;
							onTimer();
							// Set state to stop
							mmState = STATE_STOP;
							cancel(); 
						}
					}
					catch(Exception e) {
						// Nothing to do
					}
				}
			}
		}

		private void cancel() {
			mmmbQuit = true;
		}
	}
}
