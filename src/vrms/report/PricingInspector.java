package vrms.report;

import vrms.contract.PricingOption;
import vrms.contract.PricingPolicy;

public class PricingInspector{
    public static void inspectPricingPolicy(Class<? extends PricingPolicy> policyClass){
        System.out.println("Pricing Policy :");

        if (policyClass.isAnnotationPresent(PricingOption.class)){
            PricingOption annotation = policyClass.getAnnotation(PricingOption.class);
            System.out.println("Policy: "+annotation.name());
            System.out.println("Class: "+policyClass.getSimpleName());
        }else{
            System.out.println("No Pricing option annotation found");
        }
    }
}
