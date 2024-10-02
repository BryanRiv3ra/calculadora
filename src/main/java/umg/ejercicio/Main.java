package umg.ejercicio;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {

            System.out.println("Selecciona una opción:");
            System.out.println("1. Calcular área bajo la curva en el eje X (integral numérica)");
            System.out.println("2. Calcular área bajo la curva en el eje Y (integral numérica)");
            System.out.println("3. Salir");

            int metodo = scanner.nextInt();

            switch (metodo) {
                case 1:
                    System.out.print("Ingresa la función a integrar (en términos de x, ej. x^2): ");
                    scanner.nextLine();
                    String funcionNumericaX = scanner.nextLine();

                    // Pedir los límites de integración
                    System.out.print("Ingresa el límite inferior de integración: ");
                    double limiteInferiorX = scanner.nextDouble();

                    System.out.print("Ingresa el límite superior de integración: ");
                    double limiteSuperiorX = scanner.nextDouble();

                    calcularAreaBajoLaCurva(funcionNumericaX, limiteInferiorX, limiteSuperiorX, "x");
                    System.out.println("¿Deseas ver la gráfica de la función? (1. Sí, 2. No)");
                    int opcionGraficaX = scanner.nextInt();
                    if (opcionGraficaX == 1) {
                        graficarFuncion(funcionNumericaX, limiteInferiorX, limiteSuperiorX, "x");
                    }
                    break;

                case 2:
                    System.out.print("Ingresa la función a integrar (en términos de y, ej. y^2): ");
                    scanner.nextLine();
                    String funcionNumericaY = scanner.nextLine();

                    System.out.print("Ingresa el límite inferior de integración: ");
                    double limiteInferiorY = scanner.nextDouble();

                    System.out.print("Ingresa el límite superior de integración: ");
                    double limiteSuperiorY = scanner.nextDouble();

                    calcularAreaBajoLaCurva(funcionNumericaY, limiteInferiorY, limiteSuperiorY, "y");
                    System.out.println("¿Deseas ver la gráfica de la función? (1. Sí, 2. No)");
                    int opcionGraficaY = scanner.nextInt();
                    if (opcionGraficaY == 1) {
                        graficarFuncion(funcionNumericaY, limiteInferiorY, limiteSuperiorY, "y");
                    }
                    break;

                case 3:
                    continuar = false;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }

        System.out.println("Programa finalizado.");
    }

    private static void calcularAreaBajoLaCurva(String funcion, double limiteInferior, double limiteSuperior, String variable) {
        // Definir la función como una función univariada
        UnivariateFunction function = v -> {
            ExprEvaluator util = new ExprEvaluator();
            IExpr expr = util.evaluate(funcion.replace(variable, "(" + v + ")"));
            return expr.evalDouble();
        };

        SimpsonIntegrator integrator = new SimpsonIntegrator();

        try {
            double resultado = integrator.integrate(1000, function, limiteInferior, limiteSuperior);
            System.out.println("El área bajo la curva desde " + limiteInferior + " hasta " + limiteSuperior + " es: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al calcular el área bajo la curva.");
        }
    }
    private static void graficarFuncion(String funcion, double limiteInferior, double limiteSuperior, String variable) {
        SwingUtilities.invokeLater(() -> {
            XYSeries series = new XYSeries("f(" + variable + ")");

            ExprEvaluator util = new ExprEvaluator();

            for (double v = limiteInferior; v <= limiteSuperior; v += 0.1) {
                try {
                    IExpr expr = util.evaluate(funcion.replace(variable, "(" + v + ")"));
                    series.add(v, expr.evalDouble());
                } catch (Exception e) {
                    System.out.println("Error al evaluar la función.");
                    return;
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Gráfica de la función",
                    variable.toUpperCase(),
                    "f(" + variable + ")",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            JFrame frame = new JFrame("Gráfica");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
