package com.matrixMultiply;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class MultThreadMatrixMult {

	private static class MatrixMultiplierThread implements Runnable {

		// MultThreadMatrixMult context;
		Matrix A, B;
		Matrix resultingMatrix;

		// Thread work
		int start, end;

		public MatrixMultiplierThread(Matrix a, Matrix b,
				Matrix resultingMatrix, int start, int end) {
			// context = multThreadMatrixMult;
			A = a;
			B = b;
			this.resultingMatrix = resultingMatrix;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			// int cellId = start;
			int rowId = start;
			System.out.println(Thread.currentThread().getName()
					+ " Start at cell :" + rowId);
			// while (cellId < end) {
			while (rowId < end) {
				try {
					// calcCell(cellId);
					calcRow(rowId);
				} catch (MatrixOutOfBoundsException e) {
					// Don't do anything
				}
				// cellId++;
				rowId++;
			}
		}

		private void calcRow(int rowNum) throws MatrixOutOfBoundsException {

			double result;
			double[] col;
			double[] row = A.getRow(rowNum);
			for (int colNum = 0; colNum < resultingMatrix.getColumns(); colNum++) {

				col = B.getColumn(colNum);
				result = 0;
				for (int j = 0; j < row.length; j++) {
					result += row[j] * col[j];
				}
				resultingMatrix.setElem(rowNum, colNum, result);
			}
		}

		private void calcCell(int cellId) throws MatrixOutOfBoundsException {

			int rowNum = cellId / resultingMatrix.getColumns(), colNum = cellId
					% resultingMatrix.getColumns();

			double[] row = A.getRow(rowNum);
			double[] col = B.getColumn(colNum);

			double result = 0;
			for (int i = 0; i < row.length; i++) {
				result += row[i] * col[i];
			}

			resultingMatrix.setElem(rowNum, colNum, result);
		}
	}

	static public Matrix MatrixCalculation(Matrix a, Matrix b) {
		System.out.println("Start matrix multiplication");
		int processors = Runtime.getRuntime().availableProcessors();
		// ExecutorService executor = Executors.newFixedThreadPool(processors);
		Matrix resultingMatrix = new Matrix(a.getRows(), b.getColumns());

		// int cellCountForEachThread = resultingMatrix.getColumns()
		// * resultingMatrix.getRows() / processors;
		int rowCountForEachThread = resultingMatrix.getRows() / processors;

		List<Thread> workers = new ArrayList<Thread>();
		for (int i = 0; i < processors; i++) {
			// Runnable work = new MatrixMultiplierThread(a, b, resultingMatrix,
			// cellCountForEachThread * i, cellCountForEachThread * (i + 1));
			Runnable work = new MatrixMultiplierThread(a, b, resultingMatrix,
					rowCountForEachThread * i, rowCountForEachThread * (i + 1));

			Thread space = new Thread(work);
			space.start();
			workers.add(space);
			// executor.execute(worker);
		}
		// executor.shutdown();
		// while (!executor.isTerminated()) {
		// }
		// System.out.println("Finished all threads");
		for (Thread job : workers) {
			try {
				job.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultingMatrix;
	}

	public static void main(String[] args) {
		final int timesToTest = 10;

		int averageTime = 0;
		int counter = 0;
		while (counter++ < timesToTest) {
			long start = System.currentTimeMillis();

			Matrix A = new Matrix(), B = new Matrix();
			Matrix resCheck = new Matrix();
			try {
				B.readMatrixFromFile("left");
				A.readMatrixFromFile("right");
				resCheck.readMatrixFromFile("result");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Matrix res = MultThreadMatrixMult.MatrixCalculation(B, A);
			try {
				res.writeMatrixToFile("final");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long computationTime = System.currentTimeMillis() - start;
			System.out.println(computationTime);

			averageTime += computationTime;
			try {
				boolean compareResult = FileUtils.contentEquals(new File(
						"final"), new File("result"));
				// System.out.println(compareResult);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			computationTime /= timesToTest;
			System.out.println("---------------------\n---------------------");
			System.out.println("Average computatio time for " + timesToTest
					+ "tests : " + computationTime);
		}
	}
}
