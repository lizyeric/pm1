package com.sg.pcrf.statistic.file

import com.sg.pcrf.statistic.util.Constants

public class StatisticFile {

	private FileWriter policy_rule_mapping = null;
	private FileWriter policy_statistic = null;
	private FileWriter rule_statistic = null;

	public void createFile(String date) {
		try {
			policy_rule_mapping = new FileWriter(Constants.statisticFilePath + Constants.policy_rule_mappingFile + "_"
					+ date + "." + Constants.fileType);
			policy_statistic = new FileWriter(Constants.statisticFilePath + Constants.policy_statisticFile + "_" + date
					+ "." + Constants.fileType);
			rule_statistic = new FileWriter(
					Constants.statisticFilePath + Constants.rule_statisticFile + "_" + date + "." + Constants.fileType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeMappingFile(String str) {
		try {
			policy_rule_mapping.append(str);
			policy_rule_mapping.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writePolicyFile(String str) {
		try {
			policy_statistic.append(str);
			policy_statistic.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeRuleFile(String str) {
		try {
			rule_statistic.append(str);
			rule_statistic.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		if (null != policy_rule_mapping) {
			try {
				policy_rule_mapping.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != policy_statistic) {
			try {
				policy_statistic.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (null != rule_statistic) {
			try {
				rule_statistic.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
