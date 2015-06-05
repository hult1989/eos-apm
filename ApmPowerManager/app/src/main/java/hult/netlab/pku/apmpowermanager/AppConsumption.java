package hult.netlab.pku.apmpowermanager;


import java.util.ArrayList;

/**
 * Created by hult on 6/4/15.
 */
public class AppConsumption {
    public String pkgName;
    private String label;
    public long cellularUpload;
    public long wifiUpload;
    public long wifiDownload;
    public long cellularDownload;
    private ArrayList cpuConsumption;

    public AppConsumption(){
        this.pkgName = "";
        this.cellularUpload = 0;
        this.wifiDownload = 0;
        this.cellularDownload = 0;
        this.wifiUpload =0;
        this.cpuConsumption = new ArrayList();
        for(int i = 0; i < 24; i++){
            long t = 0;
            this.cpuConsumption.add(t);
        }
    }
    public void addLabel(String label){
        this.label = label;
    }
    public String getLabel(){
        return this.label;
    }
    public void addCpuComsumption (long i){
        cpuConsumption.add(i);
        if(cpuConsumption.size() > 24)
            cpuConsumption.remove(0);
    }
    public ArrayList getCpuConsumption(){
        return cpuConsumption;
    }
}
