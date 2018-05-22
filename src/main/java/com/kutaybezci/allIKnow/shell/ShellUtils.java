package com.kutaybezci.allIKnow.shell;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatcher;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.SimpleVerticalAligner;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

public class ShellUtils {
	public static final String DATE_FORMAT[] = { "dd.MM.yyyy" };
	public static final String DEFAULT_VALUE = "NOT_SET";

	public static boolean isSet(String input) {
		return !StringUtils.equals(DEFAULT_VALUE, input);
	}
	
	public static Table table(String data[][]) {
		TableModel model = new ArrayTableModel(data);
		TableBuilder tableBuilder = new TableBuilder(model);
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				tableBuilder.on(at(r, c)).addAligner(SimpleHorizontalAligner.values()[c]);
				tableBuilder.on(at(r, c)).addAligner(SimpleVerticalAligner.values()[r]);
			}
		}
		return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
	}

	public static CellMatcher at(final int theRow, final int col) {
		return new CellMatcher() {
			@Override
			public boolean matches(int row, int column, TableModel model) {
				return row == theRow && column == col;
			}
		};
	}
}
