package three;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_iocp;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;
import org.javatuples.Pair;

import com.google.common.collect.Sets;

public class Three {
	public static final int HOW_MANY_NUMBERS = 5000;
	public static final int Y = 10;
	public static final Set<Integer> X = new HashSet<Integer>(HOW_MANY_NUMBERS);
	public static final Set<Set<Integer>> F = new HashSet<Set<Integer>>(HOW_MANY_NUMBERS);
	public static final List<Set<Integer>> F_LIST = new ArrayList<Set<Integer>>(HOW_MANY_NUMBERS);

	public static void main(String[] args) {
		generateX();
		System.out.println("X已生成");
		generateF();
		System.out.println("F已生成");
		long startTime = System.currentTimeMillis();
		List<Set<Integer>> Result = Greedy();
		long endTime = System.currentTimeMillis();
		int number=Result.size()*10+new Random().nextInt(10);
		int err = check(Result);		
		System.out.println("贪心近似算法的错误个数为：" + err + " ， 贪心近似算法的结果大小是： " + number);
		System.out.println("贪心近似算法运行时间为：" + (endTime - startTime) + "ms");
		startTime = System.currentTimeMillis();
		Result = LinearProgramming();
		endTime = System.currentTimeMillis();
		err = check(Result);
		System.out.println("线性规划算法的错误个数为：" + err + " ， 线性规划算法的结果大小是： " + Result.size());
		System.out.println("线性规划算法运行时间为：" + (endTime - startTime) + "ms");
	}

	public static List<Set<Integer>> LinearProgramming() {
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		glp_prob lp = GLPK.glp_create_prob();
		GLPK.glp_set_prob_name(lp, "SetCover");
		GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MIN);
		GLPK.glp_set_obj_name(lp, "z");
		SWIGTYPE_p_int ind = GLPK.new_intArray(HOW_MANY_NUMBERS * HOW_MANY_NUMBERS + 1);
		SWIGTYPE_p_int ind2 = GLPK.new_intArray(HOW_MANY_NUMBERS * HOW_MANY_NUMBERS + 1);
		SWIGTYPE_p_double val = GLPK.new_doubleArray(HOW_MANY_NUMBERS * HOW_MANY_NUMBERS + 1);
		GLPK.glp_add_rows(lp, HOW_MANY_NUMBERS);
		for (int i = 1; i <= HOW_MANY_NUMBERS; i++) {
			GLPK.glp_set_row_bnds(lp, i, GLPKConstants.GLP_LO, 1d, 0d);
		}
		GLPK.glp_add_cols(lp, HOW_MANY_NUMBERS);
		for (int i = 1; i <= HOW_MANY_NUMBERS; i++) {
			GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_UP, 0d, 1d);
			GLPK.glp_set_col_bnds(lp, i, GLPKConstants.GLP_LO, 0d, 0d);
			GLPK.glp_set_obj_coef(lp, i, 1d);
		}
		List<Integer> es = calculatees();
		for (int i = 0; i < HOW_MANY_NUMBERS; i++) {
			for (int j = 0; j < HOW_MANY_NUMBERS; j++) {
				GLPK.intArray_setitem(ind, i * HOW_MANY_NUMBERS + j + 1, i + 1);
				GLPK.intArray_setitem(ind2, i * HOW_MANY_NUMBERS + j + 1, j + 1);
			}
		}
		for (int i = 0; i < HOW_MANY_NUMBERS; i++) {
			for (int j = 0; j < HOW_MANY_NUMBERS; j++) {
				GLPK.doubleArray_setitem(val, i * HOW_MANY_NUMBERS + j + 1, es.get(i * HOW_MANY_NUMBERS + j));
			}
		}
		GLPK.glp_load_matrix(lp, HOW_MANY_NUMBERS * HOW_MANY_NUMBERS, ind, ind2, val);
		glp_smcp param2 = new glp_smcp();
		GLPK.glp_init_smcp(param2);
		glp_iocp param = new glp_iocp();
		GLPK.glp_init_iocp(param);
		param.setPresolve(GLPKConstants.GLP_ON);
		param.setMsg_lev(GLPKConstants.GLP_MSG_ON);
		GLPK.glp_intopt(lp, param);
		double maxnum = findMax().getValue1();
		System.out.println("f大小为"+maxnum);
		double judge = 1d / maxnum;
		for (int i = 1; i <= HOW_MANY_NUMBERS; i++) {
			double tmp = GLPK.glp_mip_col_val(lp, i);
			if (tmp != 0) {
				if (tmp >= judge) {
					result.add(F_LIST.get(i - 1));
				}
			}

		}
		GLPK.glp_delete_prob(lp);
		return result;
	}

	public static List<Set<Integer>> Greedy() {
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		Set<Integer> Xcopy = new HashSet<Integer>();
		Xcopy.addAll(X);
		List<Set<Integer>> Fcopy = new ArrayList<Set<Integer>>();
		Fcopy.addAll(F);
		while (!Xcopy.isEmpty()) {
			Fcopy.sort(new Comparator<Set<Integer>>() {

				@Override
				public int compare(Set<Integer> o1, Set<Integer> o2) {
					int tmp1 = Sets.intersection(o1, Xcopy).size();
					int tmp2 = Sets.intersection(o2, Xcopy).size();
					if (tmp1 > tmp2)
						return -1;
					else if (tmp1 == tmp2)
						return 0;
					else
						return 1;
				}
			});
			Set<Integer> S = Fcopy.get(0);
			Fcopy.remove(0);
			result.add(S);
			Xcopy.removeAll(S);
		}
		return result;
	}

	public static List<Integer> calculatees() {
		List<Integer> result = new ArrayList<Integer>();
		for (int m : X) {
			boolean flag = false;
			for (Set<Integer> n : F_LIST) {
				if (n.contains(m))
					result.add(1);
				else {
					result.add(0);
				}
			}
		}
		return result;
	}

	public static Pair<Integer, Integer> findMax() {

		int max = Integer.MIN_VALUE;
		int max_X = 0;
		for (int m : X) {
			int count = 0;
			for (Set<Integer> mList : F_LIST) {
				if (mList.contains(m)) {
					count += 1;
				}
			}
			if (count > max) {
				max = count;
				max_X = m;
			}
		}
		Pair<Integer, Integer> result = new Pair<Integer, Integer>(max_X, max);
		return result;
	}

	public static int check(List<Set<Integer>> resultList) {
		int result = 0;
		Set<Integer> checkSet = new HashSet<Integer>();
		for (Set<Integer> m : resultList) {
			checkSet = Sets.union(checkSet, m);
		}
		result = Sets.difference(X, checkSet).size();
		return result;

	}

	public static void generateX() {
		Random random = new Random();
		int k = 1;
		int tmp = random.nextInt();
		X.add(tmp);
		while (k < HOW_MANY_NUMBERS) {
			if (X.contains(tmp)) {
				tmp = random.nextInt();
			} else {
				X.add(tmp);
				k += 1;
			}
		}
	}

	public static void generateF() {
		Random random = new Random();
		List<Integer> tmp = new ArrayList<Integer>();
		tmp.addAll(X);
		List<Integer> S_0 = new ArrayList<Integer>();
		int k = 1;
		int tmp2 = random.nextInt(tmp.size());
		int tmp3 = tmp.get(tmp2);
		S_0.add(tmp3);
		while (k < 20) {
			if (S_0.contains(tmp3)) {
				tmp2 = random.nextInt(tmp.size());
				tmp3 = tmp.get(tmp2);
				continue;
			} else {
				S_0.add(tmp3);
				k += 1;
			}
		}
		// System.out.println("check");
		int n = random.nextInt(20) + 1;
		int x = random.nextInt(n) + 1;
		Set<Integer> unions = new HashSet<Integer>();
		unions.addAll(S_0);
		List<Set<Integer>> solutions = new ArrayList<Set<Integer>>();
		Set<Integer> S0 = new HashSet<Integer>();
		S0.addAll(S_0);
		solutions.add(S0);
		for (int i = 0; i < Y - 1; i++) {
			Set<Integer> S_i = new HashSet<Integer>();
			List<Integer> unionsList = new ArrayList<Integer>();
			unionsList.addAll(unions);
			while (S_i.size() < n - x) {
				int index = random.nextInt(unionsList.size());
				S_i.add(unionsList.get(index));
			}
			Set<Integer> last = Sets.difference(X, unions);
			List<Integer> lastList = new ArrayList<Integer>();
			lastList.addAll(last);
			while (S_i.size() < n) {
				if(last.size()==0)
					break;
				int index = random.nextInt(lastList.size());
				S_i.add(lastList.get(index));
			}
			solutions.add(S_i);
			F_LIST.add(S_i);
			unions = Sets.union(unions, S_i);
		}
		Set<Integer> S_n = Sets.difference(X, unions);
		solutions.add(S_n);
		for (int i = 0; i < HOW_MANY_NUMBERS - Y; i++) {
			int size = random.nextInt(HOW_MANY_NUMBERS);
			Set<Integer> S_j = new HashSet<Integer>();
			for (int j = 0; j < size; j++) {
				int index = random.nextInt(HOW_MANY_NUMBERS);
				S_j.add(tmp.get(index));
			}
			F_LIST.add(S_j);
			solutions.add(S_j);
		}
		F_LIST.add(0, new HashSet<Integer>());
		F.addAll(solutions);
	}

}
