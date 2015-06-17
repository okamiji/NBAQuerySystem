package nbaquery_stats;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

public class SignTest {
	public int get_trend(Float[] data){

		float a = 0.05f;
		
		double[] d_data = new double[data.length];
		for(int i=0; i<data.length; i++){
			d_data[i] = (double) ((float) data[i]);
		}
		
		double[] new_d_data = new double[data.length - 1];
		for(int i=0; i<(data.length - 1); i++){
			new_d_data[i] = data[i];
		}
		
		float m0 = (float) d_data[data.length - 1];
		
		if(data.length <= 5){
			return 0;
		}
		
		Mean mean_ = new Mean();
		float mean = (float) mean_.evaluate(new_d_data);
		Variance v = new Variance();
		float variance = (float) v.evaluate(new_d_data);
		
		float t = (float) Math.abs((mean - m0) / (Math.sqrt(variance) / (Math.sqrt(data.length - 1))));
		float t_value = TDistribution.get_tdistribution(a / 2 + ";" + (data.length - 1));
		
		if(t < t_value){
			//差异不显著
			return -1;
		}
		else{
			return 1;
		}
		
	}
}
