package nbaquery_stats;

import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class VarianceTest {
	public static int get_variance_trend(Float[] data){
		float a = 0.05f;
		
		double[] d_data = new double[data.length];
		for(int i=0; i<data.length; i++){
			d_data[i] = (double) ((float) data[i]);
		}
		
		double[] new_d_data = null;
		double[] sample_data = null;
		
		if(data.length <= 5){
			return 0;
		}
		else if(data.length <= 8){
			//calculate the latest matches as new sample
			new_d_data = new double[data.length - 3];
			sample_data = new double[3];
			for(int i=0; i<(data.length - 3); i++){
				new_d_data[i] = d_data[i];
			}
			for(int i=0; i<3; i++){
				sample_data[i] = new_d_data[i];
			}
		}
		else{
			new_d_data = new double[data.length - 5];
			sample_data = new double[5];
			for(int i=0; i<(data.length - 5); i++){
				new_d_data[i] = d_data[i];
			}for(int i=0; i<5; i++){
				sample_data[i] = new_d_data[i];
			}
		}
		
		//需要检验是否是正态分布
		
		Variance v = new Variance();
		float variance = (float) v.evaluate(new_d_data);
		float sample_variance = (float) v.evaluate(sample_data);
		
		double chi_square = (data.length - 1) * sample_variance / variance;
		double p = ChiSquare.chisqr2pValue(data.length - 1, chi_square);
		
		if(p < a){
			//没啥波动
			return -1;
		}
		if(p == a){
			return 0;
		}
		else{
			//波动较大
			return 1;
		}
	}
	
/*	public static void main(String[] args){
	//	float[] data = {1f, 1.1f, 1.5f, 1.1f, 1.2f, 1.3f, 1.4f, 1f, 1.24f, 1f};
		float[] data = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
		System.out.println(get_variance_trend(data));
	}*/
}
