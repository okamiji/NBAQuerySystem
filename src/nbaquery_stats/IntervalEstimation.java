package nbaquery_stats;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class IntervalEstimation {
	public static float[] get_interval(float[] data){
		float a = 0.95f;
		
		double[] d_data = new double[data.length];
		for(int i=0; i<data.length; i++){
			d_data[i] = (double) ((float) data[i]);
		}
		
		Mean mean_ = new Mean();
		float mean = (float) mean_.evaluate(d_data);
		StandardDeviation standard_deviation_ = new StandardDeviation();
		float standard_deviation = (float) standard_deviation_.evaluate(d_data);

		//TODO
		float f_value = nbaquery_stats.TDistribution.get_tdistribution((1 - a) / 2 + ";" + (data.length - 1));
		float f = (float) ((float) (standard_deviation * f_value) / (Math.sqrt(data.length)));
		
		float[] return_f = new float[2];
		return_f[0] = mean - f;
		return_f[1] = mean + f;
		return return_f;
	}
/*	public static void main(String[] args){
		float[] data = {112.5f, 102f, 97.8f, 101.5f, 102f,
				108.8f, 101.6f, 108.6f, 98.4f, 100.5f,
				115.6f, 102.2f, 105f, 93.3f, 102.6f,
				123.5f, 95.4f, 102.8f, 103f, 95f};
		float[] result = get_interval(data);
		System.out.println(result[0] + " " + result[1]);
	}*/
}
