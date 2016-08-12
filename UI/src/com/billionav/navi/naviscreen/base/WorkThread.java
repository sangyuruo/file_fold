package com.billionav.navi.naviscreen.base;

import java.util.ArrayList;

public class WorkThread implements Runnable {
	
	private boolean canRun = true;
	
	private static final int interval = 200;

	@Override
	public void run() {
		while(canRun) {
			if(hasTask()) {
				handleTask();
			} else {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void handleTask() {
		for(Task t: taskList) {
			runTask(t);
		}
	}

	private boolean hasTask() {
		return taskList.size()>0;
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		canRun = false;
	}
	
	public void addTask(Runnable task, int times) {
		taskList.add(new Task(task, times));
	}
	
	private final ArrayList<Task> taskList = new ArrayList<WorkThread.Task>();
	
	private class Task{
		int times;
		final Runnable task;
		public Task(Runnable task, int times) {
			super();
			this.times = times;
			this.task = task;
		}
		
	}
	
	private void runTask(Task task) {
		task.times -- ;
		new Thread(task.task).start();
		if(task.times == 0) {
			taskList.remove(task);
		}
	}

}
