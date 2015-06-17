package nbaquery_stats;

public class ChiSquare {
	public static double chisqr2pValue(int dof, double chi_squared) {
        if (chi_squared < 0 || dof < 1) {
            return 0.0;
        }
        double k = ((double) dof) * 0.5;
        double v = chi_squared * 0.5;
        if (dof == 2) {
            return Math.exp(-1.0 * v);
        }
        double incompleteGamma = log_igf(k,v);
        // 如果过小或者非数值或者无穷
        if (Math.exp(incompleteGamma) <= 1e-8
               || Double.isNaN(Math.exp(incompleteGamma))
               || Double.isInfinite(Math.exp(incompleteGamma))) {
            return 1e-14;
        }
        double gamma = Math.log(getApproxGamma(k));
       incompleteGamma -= gamma;
        if(Math.exp(incompleteGamma) > 1){
            return 1e-14;
        }
        double pValue = 1.0 - Math.exp(incompleteGamma);
        return (double) pValue;
	}
	public static double getApproxGamma(double n) {
        // RECIP_E = (E^-1) = (1.0 / E)
        double RECIP_E = 0.36787944117144232159552377016147;
        // TWOPI = 2.0 * PI
        double TWOPI = 6.283185307179586476925286766559;
        double d = 1.0 / (10.0 * n);
        d = 1.0 / ((12* n) - d);
        d = (d + n) *RECIP_E;
        d = Math.pow(d,n);
        d *= Math.sqrt(TWOPI/ n);
        return d;
    }
	 public static double log_igf(double s, double z) {
        if (z < 0.0) {
            return 0.0;
        }
        double sc = (Math.log(z) * s) - z - Math.log(s);
        double k = KM(s, z);
        return Math.log(k) + sc;
        }
	   
    private static double KM(double s, double z) {
        double sum = 1.0;
        double nom = 1.0;
        double denom = 1.0;
        double log_nom = Math.log(nom);
        double log_denom = Math.log(denom);
        double log_s = Math.log(s);
        double log_z = Math.log(z);
        for (int i = 0; i < 1000; ++i) {
           log_nom += log_z;
           s++;
           log_s = Math.log(s);
           log_denom += log_s;
            double log_sum = log_nom - log_denom;
           sum += Math.exp(log_sum);
        }
        return sum;
    }
}
