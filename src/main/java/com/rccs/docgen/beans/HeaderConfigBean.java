package com.rccs.docgen.beans;

import com.rccs.docgen.enums.HeaderType;
import com.rccs.docgen.enums.TableType;

public class HeaderConfigBean {
	private HeaderType headerType;
	private TableType tableType;

	public HeaderConfigBean(HeaderType headerType, TableType tableType) {
		this.headerType = headerType;
		this.tableType = tableType;
	}

	public HeaderType getHeaderType() {
		return headerType;
	}

	public void setHeaderType(HeaderType headerType) {
		this.headerType = headerType;
	}

	public TableType getTableType() {
		return tableType;
	}

	public void setTableType(TableType tableType) {
		this.tableType = tableType;
	}

}
