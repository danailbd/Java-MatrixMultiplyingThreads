package com.matrixMultiply;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Matrix {

	private int rows;
	private int columns;
	private double mat[][];

	/*
	 * public Matrix() { this.rows = 0; this.columns = 0; mat = new
	 * Double[rows][columns]; }
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		mat = new double[rows][columns];
	}

	public Matrix() {
	}

	public double[] getRow(int r) throws MatrixOutOfBoundsException {
		if (r >= rows) {
			throw new MatrixOutOfBoundsException();
		}
		return mat[r];
	}

	public double[] getColumn(int c) throws MatrixOutOfBoundsException {
		if (c >= columns) {
			throw new MatrixOutOfBoundsException();
		}
		double[] result = new double[rows];

		for (int i = 0; i < result.length; i++) {
			result[i] = mat[i][c];
		}

		return result;
	}

	public final int getRows() {
		return rows;
	}

	public final int getColumns() {
		return columns;
	}

	public void setElem(int elemRow, int elemCol, double info) {
		mat[elemRow][elemCol] = info;
	}

	public final Double getElem(int elemRow, int elemCol) {
		return mat[elemRow][elemCol];
	}

	public void readMatrixFromFile(String fileName) throws IOException {

		try (FileInputStream fileInputStream = new FileInputStream(fileName);
				DataInputStream dis = new DataInputStream(fileInputStream);) {

			rows = dis.readInt();
			columns = dis.readInt();
			mat = new double[rows][columns];
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < columns; j++) {
					mat[i][j] = dis.readDouble();
				}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void writeMatrixToFile(String fileName) throws IOException {

		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
				DataOutputStream dos = new DataOutputStream(fileOutputStream)) {
			dos.writeInt(rows);
			dos.writeInt(columns);
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < columns; j++) {
					dos.writeDouble(mat[i][j]);
				}
		}
	}
}
