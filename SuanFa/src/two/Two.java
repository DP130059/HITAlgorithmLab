package two;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.javatuples.Pair;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Two {
	public static final String MAZE_FILE_PATH_1 = "input/2/inputMaze1.txt";
	public static final String MAZE_FILE_PATH_2="input/2/inputMaze2.txt";
	public static final int ROW_HEIGHT=525;
	public static final int COLUMN_WIDTH=5;
	public static final double RIVER_WEIGHT=15d;
	public static final double DESERT_WEIGHT=20d;
	public static int MAZE_M;
	public static int MAZE_N;
	public static Maze MAZE;
	public static Pair<Integer,Integer> start;
	public static Pair<Integer,Integer> end;
	public static final String MAZE_XLS_PATH = "result/2/Maze.xls";
	public static final String ASTAR_XLS_PATH = "result/2/Astar.xls";
	public static final String BIASTAR_XLS_PATH = "result/2/BiAstar.xls";

	public static void main(String[] args) {
		System.out.println("请输入迷宫横坐标大小M");
		Scanner scanner = new Scanner(System.in);
		MAZE_M = Integer.parseInt(scanner.nextLine());
		System.out.println("请输入迷宫纵坐标大小N");
		MAZE_N = Integer.parseInt(scanner.nextLine());
		MAZE = new Maze(MAZE_M, MAZE_N, MAZE_FILE_PATH_2);
		drawMaze();
		System.out.println("请输入起点横坐标");
		int startX = Integer.parseInt(scanner.nextLine());
		System.out.println("请输入起点纵坐标");
		int startY = Integer.parseInt(scanner.nextLine());
		System.out.println("请输入终点横坐标");
		int endX = Integer.parseInt(scanner.nextLine());
		System.out.println("请输入终点纵坐标");
		int endY = Integer.parseInt(scanner.nextLine());
		start = new Pair<Integer, Integer>(startX, startY);
		end = new Pair<Integer, Integer>(endX, endY);
		List<Cube> AstarResult = Astar();
		drawResult(AstarResult, true, null);
		System.out.println("A*算法计算完成，结果存入Astar.xls中");
		List<Cube>BiAstarResult=BiAstar(MAZE_FILE_PATH_2);
		Cube middle=BiAstarResult.get(0);
		drawResult(BiAstarResult, false,middle);
		System.out.println("双向A*算法计算完成，结果存入BiAstar.xls中");
	}
	
	public static List<Cube>BiAstar(String MazePath){
		List<Cube>result=new ArrayList<Cube>();
		List<Cube>openList1=new ArrayList<Cube>();
		List<Cube>openList2=new ArrayList<Cube>();
		List<Cube>closeList1=new ArrayList<Cube>();
		List<Cube>closeList2=new ArrayList<Cube>();
		Cube startCube1 = MAZE.get(start.getValue0(), start.getValue1());
		Cube endCube1 = MAZE.get(end.getValue0(), end.getValue1());
		MAZE.setHvalues(endCube1);
		Maze BiMAZE=new Maze(MAZE_M, MAZE_N, MazePath);
		Cube startCube2 = MAZE.get(start.getValue0(), start.getValue1());
		Cube endCube2 = MAZE.get(end.getValue0(), end.getValue1());
		BiMAZE.setHvalues(startCube2);
		openList1.add(startCube1);
		openList2.add(endCube2);
		while (true) {
			if (openList1.size() == 0 && openList2.size()==0) {
				System.out.println("没有路径");
				break;
			} else if (judge(openList1, openList2)) {
				break;
			} else {
				openList1.sort(new Comparator<Cube>() {
					@Override
					public int compare(Cube o1, Cube o2) {
						if (o1.getF_value() < o2.getF_value())
							return -1;
						else if(o1.getF_value() == o2.getF_value())
							return 0;
						else
							return 1;
					}
				});
				Cube a = openList1.get(0);
				List<Cube> aSurrounds = MAZE.getArround(a);
				openList1.remove(a);
				closeList1.add(a);
				for (Cube m : aSurrounds) {
					if (closeList1.contains(m))
						continue;
					else if (!openList1.contains(m)) {
						double tmp = (double) Math.abs(m.getM() - a.getM()) + Math.abs(m.getN() - a.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=m.getCost();
						m.setG_value(tmp2+a.getG_value());
						m.setParent(a);
						openList1.add(m);
					} else if (openList1.contains(m)) {
						double tmp = (double) Math.abs(m.getM() - a.getM()) + Math.abs(m.getN() - a.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=a.getG_value();
						if (tmp2<m.getG_value()) {
							//G+=tmp2+m.getCost();
							m.setParent(a);
							m.setG_value(tmp2+m.getCost());
						}
					}
				}
				
				openList2.sort(new Comparator<Cube>() {
					@Override
					public int compare(Cube o1, Cube o2) {
						if (o1.getF_value() < o2.getF_value())
							return -1;
						else if(o1.getF_value() == o2.getF_value())
							return 0;
						else
							return 1;
					}
				});
				Cube b = openList2.get(0);
				List<Cube> bSurrounds = BiMAZE.getArround(b);
				openList2.remove(b);
				closeList2.add(b);
				for (Cube m : bSurrounds) {
					if (closeList2.contains(m))
						continue;
					else if (!openList2.contains(m)) {
						double tmp = (double) Math.abs(m.getM() -b.getM()) + Math.abs(m.getN() - b.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=m.getCost();
						m.setG_value(tmp2+b.getG_value());
						m.setParent(b);
						openList2.add(m);
					} else if (openList2.contains(m)) {
						double tmp = (double) Math.abs(m.getM() - b.getM()) + Math.abs(m.getN() - b.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=b.getG_value();
						if (tmp2<m.getG_value()) {
							//G+=tmp2+m.getCost();
							m.setParent(b);
							m.setG_value(tmp2+m.getCost());
						}
					}
				}
				
			}
		}
		Cube local = endCube2;
		while (local != null) {
			result.add(local);
			local = local.getParent();
		}
		local=find(openList1, openList2).getParent();
		while (local != null) {
			result.add(local);
			local = local.getParent();
		}
		local=find(openList1, openList2).getParent().copy();
		result.add(0, local);
		return result;
	}
	public static List<Cube> Astar() {
		List<Cube> result = new ArrayList<Cube>();
		List<Cube> openList = new ArrayList<Cube>();
		List<Cube> closeList = new ArrayList<Cube>();
		Cube startCube = MAZE.get(start.getValue0(), start.getValue1());
		Cube endCube = MAZE.get(end.getValue0(), end.getValue1());
		MAZE.setHvalues(endCube);
		openList.add(startCube);
		while (true) {
			if (openList.size() == 0 && !openList.contains(endCube)) {
				System.out.println("没有路径");
				break;
			} else if (openList.contains(endCube)) {
				break;
			} else {
				openList.sort(new Comparator<Cube>() {
					@Override
					public int compare(Cube o1, Cube o2) {
						if (o1.getF_value() < o2.getF_value())
							return -1;
						else if(o1.getF_value() == o2.getF_value())
							return 0;
						else
							return 1;
					}
				});
				Cube a = openList.get(0);
				List<Cube> aSurrounds = MAZE.getArround(a);
				openList.remove(a);
				closeList.add(a);
				for (Cube m : aSurrounds) {
					if (closeList.contains(m))
						continue;
					else if (!openList.contains(m)) {
						double tmp = (double) Math.abs(m.getM() - a.getM()) + Math.abs(m.getN() - a.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=m.getCost();
						m.setG_value(tmp2+a.getG_value());
						m.setParent(a);
						openList.add(m);
					} else if (openList.contains(m)) {
						double tmp = (double) Math.abs(m.getM() - a.getM()) + Math.abs(m.getN() - a.getN());
						double tmp2=0d;
						if (tmp == 2) {
							tmp2+=14.14d;
						} else {
							tmp2+=10d;
						}						
						tmp2+=a.getG_value();
						if (tmp2<m.getG_value()) {
							//G+=tmp2+m.getCost();
							m.setParent(a);
							m.setG_value(tmp2+m.getCost());
						}
					}
				}
			}
		}
		Cube local = endCube;
		while (local != null) {
			result.add(local);
			local = local.getParent();
		}
		return result;
	}

	public static boolean judge(List<Cube>openList1,List<Cube>openList2) {
		boolean result=false;
		for(Cube m:openList1) {
			for(Cube n:openList2) {
				if(m.getM()==n.getM()&&m.getN()==n.getN())
					result=true;
			}
		}
		return result;
	}
	public static Cube find(List<Cube>openList1,List<Cube>openList2) {
		Cube result=null;
		for(Cube m:openList1) {
			for(Cube n:openList2) {
				if(m.getM()==n.getM()&&m.getN()==n.getN())
					result=m;
			}
		}
		return result;
	}
	public static void drawMaze() {
		try {
			WritableWorkbook mazeWorkBook = Workbook.createWorkbook(new File(MAZE_XLS_PATH));
			WritableSheet originSheet = mazeWorkBook.createSheet("原数据", 0);
			for (int i = 0; i < MAZE_M; i++) {
				originSheet.setRowView(i, ROW_HEIGHT);
				for (int j = 0; j < MAZE_N; j++) {
					originSheet.setColumnView(j, COLUMN_WIDTH);
					Cube tmpCube = MAZE.get(i, j);
					double cost = tmpCube.getCost();
					WritableCellFormat white = new WritableCellFormat();
					white.setBackground(Colour.WHITE);
					white.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat grey = new WritableCellFormat();
					grey.setBackground(Colour.DARK_RED);
					grey.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat yellow = new WritableCellFormat();
					yellow.setBackground(Colour.YELLOW);
					yellow.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat blue = new WritableCellFormat();
					blue.setBackground(Colour.BLUE);
					blue.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					Label tmpCell = new Label(j, i, "");
					if (cost == 0)
						tmpCell.setCellFormat(white);
					else if (cost == Integer.MAX_VALUE)
						tmpCell.setCellFormat(grey);
					else if (cost == RIVER_WEIGHT)
						tmpCell.setCellFormat(blue);
					else if (cost == DESERT_WEIGHT)
						tmpCell.setCellFormat(yellow);
					originSheet.addCell(tmpCell);
				}
			}
			mazeWorkBook.write();
			mazeWorkBook.close();
			// System.out.println(Math.random());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void drawResult(List<Cube> result, Boolean flag,Cube middle) {
		try {
			WritableWorkbook mazeWorkBook;
			WritableSheet originSheet;
			if (flag) {
				mazeWorkBook = Workbook.createWorkbook(new File(ASTAR_XLS_PATH));
				originSheet = mazeWorkBook.createSheet("Astar", 1);
			}

			else {
				mazeWorkBook = Workbook.createWorkbook(new File(BIASTAR_XLS_PATH));
				originSheet = mazeWorkBook.createSheet("BiAstar", 1);
			}			
			for (int i = 0; i < MAZE_M; i++) {
				originSheet.setRowView(i, ROW_HEIGHT);
				for (int j = 0; j < MAZE_N; j++) {
					originSheet.setColumnView(j, COLUMN_WIDTH);
					Cube tmpCube = MAZE.get(i, j);						
					double cost = tmpCube.getCost();
					boolean flag2 = result.contains(tmpCube);
					WritableCellFormat white = new WritableCellFormat();
					white.setBackground(Colour.WHITE);
					if (flag2)
						white.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.BRIGHT_GREEN);
					else
						white.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat grey = new WritableCellFormat();
					grey.setBackground(Colour.DARK_RED);
					if (flag2)
						grey.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.BRIGHT_GREEN);
					else
						grey.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat yellow = new WritableCellFormat();
					yellow.setBackground(Colour.YELLOW);
					if (flag2)
						yellow.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.BRIGHT_GREEN);
					else
						yellow.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					WritableCellFormat blue = new WritableCellFormat();
					blue.setBackground(Colour.BLUE);
					if (flag2)
						blue.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.BRIGHT_GREEN);
					else
						blue.setBorder(Border.ALL, BorderLineStyle.DASH_DOT, Colour.RED);
					Label tmpCell = new Label(j, i, "");
					if(i==start.getValue0()&&j==start.getValue1()) {
						tmpCell.setString("起点");
					}else if(i==end.getValue0()&&j==end.getValue1()){
						tmpCell.setString("终点");
					}
					if (cost == 0)
						tmpCell.setCellFormat(white);
					else if (cost == Integer.MAX_VALUE)
						tmpCell.setCellFormat(grey);
					else if (cost == RIVER_WEIGHT)
						tmpCell.setCellFormat(blue);
					else if (cost == DESERT_WEIGHT)
						tmpCell.setCellFormat(yellow);
					if(!flag) {
						if(i==middle.getM()&&j==middle.getN()) {
							WritableCellFormat green=new WritableCellFormat();
							green.setBackground(Colour.BRIGHT_GREEN);
							green.setBorder(Border.ALL, BorderLineStyle.THICK, Colour.BRIGHT_GREEN);
							tmpCell.setCellFormat(green);
						}
					}

					originSheet.addCell(tmpCell);
				}
			}

			mazeWorkBook.write();
			mazeWorkBook.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
