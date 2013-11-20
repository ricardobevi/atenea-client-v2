package org.squadra.atenea.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * Cartel que se muestra temporalmente mientras carga el programa al iniciar.
 * Esta estructurada con Java Swing.
 * @author Leandro Morrone
 *
 */
@SuppressWarnings("serial")
public class SplashGUI extends JFrame {
	
	/** Singleton */
	private static SplashGUI INSTANCE = null;
	
	/**
	 * Crea una instancia de la interfaz, y si ya existe la devuelve
	 * @return instancia singleton de la pantalla de carga
	 * @author Leandro Morrone
	 */
	public static SplashGUI createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SplashGUI();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de la interfaz de usuario
	 * @return instancia singleton de la pantalla de carga
	 * @author Leandro Morrone
	 */
	public static SplashGUI getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: inicia la pantalla de carga
	 * @author Leandro Morrone
	 */
	private SplashGUI() {
		initComponents();
	}
		
	// Elementos swing de la interfaz de usuario
	private JLabel lblBackground;
	private JProgressBar progressBar;
	private JLabel lblVersion;
	private JLabel lblLicense;
	private JLabel lblLoading;
	private JLabel lblCloseButton;
	
	
	/**
	 * Inicializo los componentes de la pantalla de carga y la muestro en pantalla
	 * @author Leandro Morrone
	 */
	private void initComponents() {
		
		//=================== PROPIEDADES DE LA VENTANA =====================
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Atenea - Cargando...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(397, 270);
		setIconImage(Resources.Images.ateneaIcon);
		setLocationRelativeTo(null);
		setResizable(false);
		//setAlwaysOnTop(true); //TODO: leer de archivo config
		
		//========================== BACKGROUND ============================= 
		
		lblBackground = new JLabel();
		lblBackground.setIcon(Resources.Images.Backgrounds.splash_beta);
		lblBackground.setBorder(new LineBorder(Color.GRAY, 1));
		lblBackground.setBounds(0, 0, 
				lblBackground.getIcon().getIconWidth(), 
				lblBackground.getIcon().getIconHeight());

		//========================= PROGRESS BAR ============================ 
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(2, 254, 393, 15);
		
		//======================== TEXTO LICENCIA =========================== 
		
		lblLicense = new JLabel("(C) 2013 Squadra Group, GNU General Public License.");
		lblLicense.setForeground(Color.GRAY);
		lblLicense.setBounds(5, 238, 393, 15);
		
		//======================== TEXTO VERSION ============================ 
		
		lblVersion = new JLabel("1.4");
		lblVersion.setForeground(new Color(151, 153, 154));
		lblVersion.setBounds(332, 173, 40, 40);
		lblVersion.setFont(new Font("Arial", Font.BOLD, 24));
		
		//======================== TEXTO DE CARGA =========================== 
		
		lblLoading = new JLabel("Iniciando...");
		lblLoading.setForeground(new Color(70, 70, 70));
		lblLoading.setBounds(6, 222, 393, 15);
		
		//====================== BOTON PARA CERRAR ==========================
		
		lblCloseButton = new JLabel();
		lblCloseButton.setIcon(Resources.Images.CloseButton.grey_mini);
		lblCloseButton.setBounds(375, 6, 
				lblCloseButton.getIcon().getIconWidth(), 
				lblCloseButton.getIcon().getIconHeight());

		lblCloseButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	closeButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblCloseButton.setIcon(Resources.Images.CloseButton.blue_mini);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblCloseButton.setIcon(Resources.Images.CloseButton.grey_mini);
            }
        });
		
		
		//===================================================================
		// Agrego todos los elementos a la interfaz en diferentes capas
		
		JLayeredPane layeredPane = getLayeredPane();
		layeredPane.add(lblBackground, new Integer(1));   //el Integer es el z-index
		layeredPane.add(progressBar, new Integer(2));
		layeredPane.add(lblLicense, new Integer(2));
		layeredPane.add(lblVersion, new Integer(2));
		layeredPane.add(lblLoading, new Integer(2));
		layeredPane.add(lblCloseButton, new Integer(2));
		
		
		// Hago visible la GUI una vez que termino de cargar todos los componentes
		setVisible(true);
	}
	
	
	/**
	 * Se ejecuta presionando sobre el boton de cerrar.
	 * Cierra el programa
	 */
	protected void closeButtonMouseClicked() {
		System.exit(0);
	}
	
	/**
	 * Metodo que cambia el valor de la barra de progreso.
	 * @param percent Porcentaje de progreso (entre 0 y 100)
	 * @param loadingText Mensaje de lo que se esta cargando, para mostrar en pantalla
	 */
	public void setProgressBarPercent(int percent, String loadingText) {
		if (percent >= 0 || percent <= 100) {
			progressBar.setValue(percent);
			progressBar.repaint();
		}
		lblLoading.setText(loadingText);
	}
	
	/**
	 * Metodo que incrementa el valor de la barra de progreso.
	 * @param percent Porcentaje que se desea incrementar al valor actual de progreso
	 * @param loadingText Mensaje de lo que se esta cargando, para mostrar en pantalla
	 */
	public void increaseProgressBarPercent(int percent, String loadingText) {
		if (progressBar.getValue() + percent >= 0 || progressBar.getValue() + percent <= 100) {
			progressBar.setValue(progressBar.getValue() + percent);
			progressBar.repaint();
		}
		lblLoading.setText(loadingText);
	}

}
