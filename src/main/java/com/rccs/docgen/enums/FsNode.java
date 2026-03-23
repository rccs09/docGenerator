package com.rccs.docgen.enums;

import java.util.List;

public interface FsNode {
	String dir();
	List<FsNode> children();
}
