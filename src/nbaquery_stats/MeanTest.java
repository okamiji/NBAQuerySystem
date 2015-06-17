package nbaquery_stats;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class MeanTest {
	public static int get_mean_trend(Float[] data){
		float a = 0.01f;
		
		double[] d_data = new double[data.length];
		for(int i=0; i<data.length; i++){
			d_data[i] = (double) ((float) data[i]);
		}
		
		double[] new_d_data = null;
		double sample_mean = 0;
		int sample_count = data.length;
		
		if(data.length <= 5){
			return 0;
		}
		else if(data.length <= 8){
			//calculate the latest matches as new sample
			for(int i=(data.length - 3); i<data.length; i++){
				sample_mean += d_data[i];
			}
			sample_mean /= 3;
			new_d_data = new double[data.length - 3];
			for(int i=0; i<(data.length - 3); i++){
				new_d_data[i] = d_data[i];
			}
			sample_count = 3;
		}
		else{
			for(int i=(data.length - 5); i<data.length; i++){
				sample_mean += d_data[i];
			}
			sample_mean /= 5;
			new_d_data = new double[data.length - 5];
			for(int i=0; i<(data.length - 5); i++){
				new_d_data[i] = d_data[i];
			}
		}
		
		//需要检验是否是正态分布
		
		Mean mean_ = new Mean();
		float mean = (float) mean_.evaluate(new_d_data);
		StandardDeviation standard_deviation_ = new StandardDeviation();
		float standard_deviation = (float) standard_deviation_.evaluate(new_d_data);
		
		double z = (sample_mean - mean) / (standard_deviation / Math.sqrt(sample_count));
		double p = 0;
		NormalDistribution nd = new NormalDistribution(0, 1);
		if(z >= 0){
			p = 0.5 + nd.probability(0, z);
		}
		else{
			p = 0.5 + nd.probability(z, 0);
			p = 1 - p;
		}
		
		if(p >= a){
			return 1;
		}
		else{
			return -1;
		}
	}
	
/*	public static void main(String[] args){
		float[] data = {0f, 0.1f, 0.5f, 1.1f, 1.2f, 1.3f, 1.4f, 2f, 2.4f, -225f};
		System.out.println(get_mean_trend(data));
	}*/
	
}
