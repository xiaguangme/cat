package com.dianping.cat.report.page.transaction;

import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.consumer.transaction.model.entity.Duration;
import com.dianping.cat.consumer.transaction.model.entity.Range;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.report.graph.DefaultValueTranslater;
import com.dianping.cat.report.graph.ValueTranslater;

public class MobileGraphs {

	private MobileGraphItem m_duration = new MobileGraphItem();

	private MobileGraphItem m_hit = new MobileGraphItem();

	private MobileGraphItem m_avargerTime = new MobileGraphItem();

	private MobileGraphItem m_failure = new MobileGraphItem();

	private TransactionName m_name;

	private Map<Integer, Integer> m_map = new HashMap<Integer, Integer>();

	private transient ValueTranslater m_tansalater = new DefaultValueTranslater();

	private transient String[] m_xlabel = { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50","55","60" };
	public MobileGraphs() {
		int k = 1;

		m_map.put(0, 0);
		for (int i = 0; i < 12; i++) {
			m_map.put(k, i);
			k <<= 1;
		}
	}

	public MobileGraphs display(TransactionName name) {
		m_name = name;
		creatAverageGraph();
		creatDurationGraph();
		creatFailureGraph();
		creatHitGraph();
		return this;
	}

	private void creatAverageGraph() {
		double[] averageValues = loadAverageValues();
		m_avargerTime.setTitle("Average Duration Over Time");
		m_avargerTime.setValue(averageValues);
		
		double[] ylable = new double[6];
		m_avargerTime.setXlabel(m_xlabel);
		m_avargerTime.setYlable(ylable);
		double maxValue = m_tansalater.getMaxValue(averageValues);
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				ylable[0] = 0;
			} else {
				ylable[i] = maxValue / 5 * i;
			}
		}
	}

	private void creatDurationGraph() {
		double[] averageValues = loadDurationValues();
		m_duration.setTitle("Duration Distribution");
		m_duration.setValue(averageValues);
		String[] xlabel = { "0", "1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024" ,"2048" };
		double[] ylable = new double[6];
		m_duration.setXlabel(xlabel);
		m_duration.setYlable(ylable);
		double maxValue = m_tansalater.getMaxValue(averageValues);
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				ylable[0] = 0;
			} else {
				ylable[i] = maxValue / 5 * i;
			}
		}
	}

	private void creatFailureGraph() {
		double[] averageValues = loadFailureValues();
		m_failure.setTitle("Failures Over Time");
		m_failure.setValue(averageValues);
		double[] ylable = new double[6];
		m_failure.setXlabel(m_xlabel);
		m_failure.setYlable(ylable);
		double maxValue = m_tansalater.getMaxValue(averageValues);
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				ylable[0] = 0;
			} else {
				ylable[i] = maxValue / 5 * i;
			}
		}
	}

	private void creatHitGraph() {
		double[] averageValues = loadHitValues();
		m_hit.setTitle("Hits Over Time");
		m_hit.setValue(averageValues);
		double[] ylable = new double[6];
		m_hit.setXlabel(m_xlabel);
		m_hit.setYlable(ylable);
		double maxValue = m_tansalater.getMaxValue(averageValues);
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				ylable[0] = 0;
			} else {
				ylable[i] = maxValue / 5 * i;
			}
		}
	}

	private double[] loadAverageValues() {
		double[] values = new double[12];

		for (Range range : m_name.getRanges()) {
			int value = range.getValue();
			int k = value / 5;

			values[k] += range.getAvg();
		}

		return values;
	}

	protected double[] loadDurationValues() {
		double[] values = new double[13];

		for (Duration duration : m_name.getDurations()) {
			int d = duration.getValue();
			Integer k = m_map.get(d);

			if (k != null) {
				values[k] += duration.getCount();
			} else {
				values[12] = duration.getCount();
			}
		}

		return values;
	}

	protected double[] loadFailureValues() {
		double[] values = new double[12];

		for (Range range : m_name.getRanges()) {
			int value = range.getValue();
			int k = value / 5;

			values[k] += range.getFails();
		}

		return values;
	}

	protected double[] loadHitValues() {
		double[] values = new double[12];

		for (Range range : m_name.getRanges()) {
			int value = range.getValue();
			int k = value / 5;

			values[k] += range.getCount();
		}

		return values;
	}
}
