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
            // Mostrar el menú primero
            System.out.println("Selecciona una opción:");
            System.out.println("1. Calcular área bajo la curva en el eje X (integral numérica)");
            System.out.println("2. Calcular área bajo la curva en el eje Y (integral numérica)");
            System.out.println("3. Salir");

            int metodo = scanner.nextInt();

            switch (metodo) {
                case 1:
                    // Pedir al usuario que ingrese la función a integrar
                    System.out.print("Ingresa la función a integrar (en términos de x, ej. x^2): ");
                    scanner.nextLine(); // Consumir la línea nueva después de nextInt()
                    String funcionNumericaX = scanner.nextLine();

                    // Pedir los límites de integración
                    System.out.print("Ingresa el límite inferior de integración: ");
                    double limiteInferiorX = scanner.nextDouble();

                    System.out.print("Ingresa el límite superior de integración: ");
                    double limiteSuperiorX = scanner.nextDouble();

                    // Cálculo del área bajo la curva (integral numérica) usando Apache Commons Math
                    calcularAreaBajoLaCurva(funcionNumericaX, limiteInferiorX, limiteSuperiorX, "x");
                    System.out.println("¿Deseas ver la gráfica de la función? (1. Sí, 2. No)");
                    int opcionGraficaX = scanner.nextInt();
                    if (opcionGraficaX == 1) {
                        graficarFuncion(funcionNumericaX, limiteInferiorX, limiteSuperiorX, "x");
                    }
                    break;

                case 2:
                    // Pedir al usuario que ingrese la función a integrar en el eje Y
                    System.out.print("Ingresa la función a integrar (en términos de y, ej. y^2): ");
                    scanner.nextLine(); // Consumir la línea nueva después de nextInt()
                    String funcionNumericaY = scanner.nextLine();

                    // Pedir los límites de integración
                    System.out.print("Ingresa el límite inferior de integración: ");
                    double limiteInferiorY = scanner.nextDouble();

                    System.out.print("Ingresa el límite superior de integración: ");
                    double limiteSuperiorY = scanner.nextDouble();

                    // Cálculo del área bajo la curva en el eje Y (integral numérica)
                    calcularAreaBajoLaCurva(funcionNumericaY, limiteInferiorY, limiteSuperiorY, "y");
                    System.out.println("¿Deseas ver la gráfica de la función? (1. Sí, 2. No)");
                    int opcionGraficaY = scanner.nextInt();
                    if (opcionGraficaY == 1) {
                        graficarFuncion(funcionNumericaY, limiteInferiorY, limiteSuperiorY, "y");
                    }
                    break;

                case 3:
                    // Salir del programa
                    continuar = false;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }

        System.out.println("Programa finalizado.");
    }

    // Método para calcular el área bajo la curva (integral numérica) usando Apache Commons Math
    private static void calcularAreaBajoLaCurva(String funcion, double limiteInferior, double limiteSuperior, String variable) {
        // Definir la función como una función univariada
        UnivariateFunction function = v -> {
            ExprEvaluator util = new ExprEvaluator();
            IExpr expr = util.evaluate(funcion.replace(variable, "(" + v + ")"));
            return expr.evalDouble();
        };

        // Crear un integrador de Simpson
        SimpsonIntegrator integrator = new SimpsonIntegrator();

        // Realizar la integración numérica
        try {
            double resultado = integrator.integrate(1000, function, limiteInferior, limiteSuperior);
            System.out.println("El área bajo la curva desde " + limiteInferior + " hasta " + limiteSuperior + " es: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al calcular el área bajo la curva.");
        }
    }

    // Método para graficar la función y el área bajo la curva en el eje X o Y
    private static void graficarFuncion(String funcion, double limiteInferior, double limiteSuperior, String variable) {
        // Ejecutar la gráfica en un hilo separado
        SwingUtilities.invokeLater(() -> {
            XYSeries series = new XYSeries("f(" + variable + ")");

            // Evaluador de expresiones
            ExprEvaluator util = new ExprEvaluator();

            // Rellenar la serie de datos
            for (double v = limiteInferior; v <= limiteSuperior; v += 0.1) {
                try {
                    IExpr expr = util.evaluate(funcion.replace(variable, "(" + v + ")"));
                    series.add(v, expr.evalDouble());
                } catch (Exception e) {
                    System.out.println("Error al evaluar la función.");
                    return;
                }
            }

            // Crear el dataset
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            // Crear el gráfico
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Gráfica de la función",  // Título
                    variable.toUpperCase(),  // Etiqueta del eje X
                    "f(" + variable + ")",  // Etiqueta del eje Y
                    dataset,  // Datos
                    PlotOrientation.VERTICAL,
                    true,  // Mostrar leyenda
                    true,  // Usar tooltips
                    false  // Generar URLs
            );

            // Mostrar la gráfica en una ventana
            JFrame frame = new JFrame("Gráfica");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Esto asegura que solo se cierre la ventana
            frame.setLayout(new BorderLayout());
            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
