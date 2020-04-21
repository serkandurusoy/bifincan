package com.dna.bifincan.web.servlet.chart;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePageChart extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(HomePageChart.class);
    
    public HomePageChart() {
        // nothing required
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        OutputStream out = response.getOutputStream();
        
        String percenceString = request.getParameter("percentage");
        if (percenceString == null || percenceString.trim().length() == 0) {
            percenceString = "0";
        }
        int percentage = 0;
        try {
            percentage = Integer.parseInt(percenceString);
            if (percentage < 0) {
                percentage = 0;
            }
            if (percentage > 100) {
                percentage = 100;
            }
        } catch (Exception e) {
            percentage = 0;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("debug...@percetage=" + percentage);
        }
        
        try {
            
            JFreeChart chart = null;
            chart = createPieChart(percentage);
            if (chart != null) {
                response.setContentType("image/png");
                ChartUtilities.writeChartAsPNG(out, chart, 143, 103);
            }
        } catch (Exception e) {
            log.error(e.getMessage());            
            String nochartimgePath = request.getSession().getServletContext().getRealPath("/resources/images/global/noimage/nochartimage.png");
            if (log.isDebugEnabled()) {
                log.debug("nochartimage path @" + nochartimgePath);
            }
            BufferedImage _bufferedImage = ImageIO.read(new FileInputStream(nochartimgePath));
            ChartUtilities.writeBufferedImageAsPNG(out, _bufferedImage);
        } finally {
            out.close();
        }
        
    }
    
    private JFreeChart createPieChart(int percentage) {
        
        DefaultPieDataset data = new DefaultPieDataset();
        data.setValue("bigorus", new Double(percentage));
        data.setValue("others", new Double(100 - percentage));
        
        JFreeChart chart = ChartFactory.createPieChart(
                "", data, false, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setSectionPaint("bigorus", new Color(102, 204, 204));
        plot.setSectionPaint("others", new Color(89, 108, 108));
        plot.setExplodePercent("bigorus", 0.15);
        plot.setInteriorGap(0);
        plot.setSectionOutlinesVisible(false);
        plot.setStartAngle(170);
        plot.setShadowPaint(null);
        plot.setOutlinePaint(null);
        plot.setOutlineStroke(null);
        plot.setOutlineVisible(false);
        plot.setBackgroundAlpha(0.0f);
        plot.setBackgroundPaint(null);
        plot.setInsets(new RectangleInsets(UnitType.ABSOLUTE, 0.0, 0.0, 0.0, 0.0));
        chart.setBorderVisible(false);
        chart.setPadding(new RectangleInsets(UnitType.ABSOLUTE, 0.0, 0.0, 0.0, 0.0));
        chart.setBackgroundPaint(new Color(47, 60, 60));
        
        return chart;
        
    }
}
