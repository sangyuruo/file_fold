package com.billionav.DRIR.GSensorLevel;

public class DRIRGSensorLevel {
	private int id;
	private int N1;
	private int N2;
	private double A;
	private double B;
	public DRIRGSensorLevel()
	{
		N1 = 5;
		N2 = 9;
		A = 2.5;
		B = 2.0;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getN1() {
		return N1;
	}
	public void setN1(int n1) {
		N1 = n1;
	}
	public int getN2() {
		return N2;
	}
	public void setN2(int n2) {
		N2 = n2;
	}
	public double getA() {
		return A;
	}
	public void setA(double a) {
		A = a;
	}
	public double getB() {
		return B;
	}
	public void setB(double b) {
		B = b;
	}
}
