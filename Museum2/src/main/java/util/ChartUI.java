package util;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *  1在視窗建立時先呼叫 ChartUI.applyTheme(); 
 *  2使用 createBar(...)、createPie(...) 快速建立圖表
 *  3如需要，呼叫 setTitle(...) 客製標題字型
 */

public final class ChartUI {
    private ChartUI() {}

    
    public static void applyTheme() {
        StandardChartTheme theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();
        theme.setExtraLargeFont(new Font("Dialog", Font.PLAIN, 16)); // 圖例/標題等較大字
        theme.setLargeFont(new Font("Dialog", Font.PLAIN, 14));      // 軸標籤
        theme.setRegularFont(new Font("Dialog", Font.PLAIN, 12));    // 一般文字
        theme.setSmallFont(new Font("Dialog", Font.PLAIN, 10));

        theme.setTitlePaint(Color.DARK_GRAY);
        theme.setChartBackgroundPaint(Color.WHITE);
        theme.setPlotBackgroundPaint(Color.WHITE);

        ChartFactory.setChartTheme(theme);
    }

    //設定圖表標題（統一字型樣式）
    public static void setTitle(JFreeChart chart, String text) {
        if (chart == null) return;
        chart.setTitle(new TextTitle(text, new Font("Dialog", Font.BOLD, 18)));
    }

    //建立長條圖，預設：無圖例、有工具提示
    public static JFreeChart createBar(String title, String xLabel, String yLabel,
                                       DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                title, xLabel, yLabel,
                dataset,
                PlotOrientation.VERTICAL,
                false,   // legend
                true,    // tooltips
                false    // 沒有超連結urls
        );
        setTitle(chart, title);
        return chart;
    }

    // 建立圓餅圖（有圖例、工具圖示）
    public static JFreeChart createPie(String title, DefaultPieDataset<?> dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,    // legend
                true,    // tooltips
                false    // 沒有超連結 urls 
        );
        setTitle(chart, title);
        return chart;
    }
}
