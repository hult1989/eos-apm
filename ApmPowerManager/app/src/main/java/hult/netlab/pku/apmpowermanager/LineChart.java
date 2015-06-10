package hult.netlab.pku.apmpowermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Paint.Align;



//用来画app的cpu耗电的图的
public class LineChart {
    private double[] appConsume;
    private int tag;
    public LineChart(double[] appConsume, int tag) {
        this.appConsume = appConsume;
        this.tag = tag;
    }
    public LineChart(){
        this.appConsume = new double[24];
        for(int i = 23; i >=0; i--){
            this.appConsume[i] = i;
        }

    }

    public View execute(Context context) {
        double[] timeline = new double[24];
        for(int i = 0; i < 24; i++){
            timeline[i] = -23 + i;
        }
        String[] titles = {};
        if(tag == 0) {
            titles = new String[]{"CPU history"};
        }else{
            titles = new String[]{"battery history"};
        }
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(timeline);
        }
        for(int i = 0; i < appConsume.length; i++){
        }


        List<double[]> values = new ArrayList<double[]>();

        values.add(appConsume);

        int[] colors = new int[]{Color.rgb(0, 150, 136)};
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        double ymax = 0;
        double ymin = 0;
        for(int i = 0; i < 24; i++){
            if(appConsume[i] > ymax)
                ymax = appConsume[i];
            if(appConsume[i] < ymin)
                ymin = appConsume[i];
        }
        setChartSettings(renderer, "", "Last 24 hour", "", -24, 0, 0, 100, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        //double[] range = {0,, 0, 60};
    //    renderer.setRange(range);
        renderer.setGridColor(Color.LTGRAY);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setInScroll(false);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanLimits(new double[] { -24, 0, 0, 100 });
        renderer.setZoomLimits(new double[] { -24, 0,0, 100 });
        View view = ChartFactory.getLineChartView(context, buildDataset(titles, x, values), renderer);
        return view;
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        //显示标签
        renderer.setShowLabels(false);
        //不显示底部说明
        renderer.setShowLegend(false);
        //设置标签字体大小
        renderer.setLabelsTextSize(14);
        renderer.setLabelsColor(Color.RED);
        renderer.setZoomEnabled(false);
        renderer.setPanEnabled(false);
        return renderer;
    }

    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        //!!
        renderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        //renderer.setMarginsColor(Color.TRANSPARENT);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setFitLegend(true);

        renderer.setMargins(new int[] { 20, 20, 20, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }
}
