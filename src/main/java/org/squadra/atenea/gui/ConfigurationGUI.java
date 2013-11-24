package org.squadra.atenea.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import org.squadra.atenea.Atenea;

/**
 * Interfaz de usuario con la configuracion del cliente.
 * Esta estructurada con Java Swing.
 * @author Leandro Morrone
 * 
 */
@SuppressWarnings("serial")
public class ConfigurationGUI extends JFrame {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea = Atenea.getInstance();
	
	/** Singleton */
	private static ConfigurationGUI INSTANCE = null;
	
	/**
	 * Crea una instancia de la interfaz, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz de configuracion
	 * @author Leandro Morrone
	 */
	public static ConfigurationGUI createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConfigurationGUI();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de la interfaz de usuario
	 * @return instancia singleton de la interfaz de configuracion
	 * @author Leandro Morrone
	 */
	public static ConfigurationGUI getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: inicia la interfaz de configuracion
	 * @author Leandro Morrone
	 */
	private ConfigurationGUI() {
		setResizable(false);
		initComponents();
	}
	
	// Componentes de la interfaz
	private JPanel contentPane;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JLabel lblUserName;
	private JTextField textUserName;
	private JLabel lblMinPcm;
	private JLabel lblSilenceTimeout;
	private JSpinner spinnerMinPcm;
	private JSpinner spinnerSilenceTimeout;
	private JCheckBox checkAlwaysOnTop;
	private JCheckBox checkPlayResponses;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnDefaultValues;
	
	/**
	 * Inicializo los componentes de la interfaz de usuario y la muestro en pantalla
	 * @author Leandro Morrone
	 */
	private void initComponents() {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Configuración de Atenea");
		setBounds(100, 100, 432, 289);
		setAlwaysOnTop(Boolean.parseBoolean(atenea.getConfiguration().getVariable("alwaysOnTop")));
		
		// Sobre-escribo el metodo que se ejecuta cuando cierro la aplicacion
		WindowAdapter exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	INSTANCE = null;
        		dispose();
            }
        };
        addWindowListener(exitListener);
		
		
		// ===================== PANELES PARA AGRUPAR COMPONENTES ========================

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panel1 = new JPanel();
		panel1.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		
		panel2 = new JPanel();
		panel2.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		
		panel3 = new JPanel();
		panel3.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		
		
		// ========================= CAMPO NOMBRE DE USUARIO =============================
		
		lblUserName = new JLabel("Nombre del usuario:");
		textUserName = new JTextField();
		textUserName.setColumns(10);
		
		if (!atenea.getConfiguration().getVariable("userName").equals("?")) {
			textUserName.setText(atenea.getConfiguration().getVariable("userName"));
		}

		// ============================ CAMPO PCM MINIMO =================================
		
		lblMinPcm = new JLabel("Nivel de ruido mínimo para detener la grabación (PCM):");

		spinnerMinPcm = new JSpinner();
		spinnerMinPcm.setModel(new SpinnerNumberModel(
				new Float(0.04), new Float(0.01), new Float(0.50), new Float(0.01)));
		
		spinnerMinPcm.setValue(Float.parseFloat(atenea.getConfiguration().getVariable("minPcm")));
		
		// ======================== CAMPO TIMEOUT POR SILENCIO ===========================
		
		lblSilenceTimeout = new JLabel("Tiempo de espera en silencio para detener grabación (seg):");
		
		spinnerSilenceTimeout = new JSpinner();
		spinnerSilenceTimeout.setModel(new SpinnerNumberModel(3, 1, 10, 1));
		
		spinnerSilenceTimeout.setValue(Float.parseFloat(atenea.getConfiguration().getVariable("silenceTimeout")));
		
		// ================================ CHECKBOXES ===================================
		
		checkAlwaysOnTop = new JCheckBox("Pantalla siempre visible.");
		checkAlwaysOnTop.setSelected(Boolean.parseBoolean(atenea.getConfiguration().getVariable("alwaysOnTop")));
		
		checkPlayResponses = new JCheckBox("Reproducir respuestas de Atenea de forma audible.");
		checkPlayResponses.setSelected(Boolean.parseBoolean(atenea.getConfiguration().getVariable("playResponses")));
		
		
		// ========================= BOTON DE GUARDAR CAMBIOS ============================
		
		btnSave = new JButton("Guardar");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonMouseClicked();
			}
		});
		
		// ============================ BOTON DE CANCELAR ================================
		
		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonMouseClicked();
			}
		});
		
		// ================== BOTON DE RESTAURAR VALORES POR DEFECTO =====================
		
		btnDefaultValues = new JButton("Restaurar valores");
		btnDefaultValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultButtonMouseClicked();
			}
		});
		
		
		// ======================== ESTRUCTURA DE LA INTERFAZ ===========================
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
						.addComponent(panel3, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
						.addComponent(panel2, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnDefaultValues)
							.addPreferredGap(ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel2, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel3, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDefaultValues)
						.addComponent(btnCancel)
						.addComponent(btnSave))
					.addContainerGap(41, Short.MAX_VALUE))
		);
		
		GroupLayout gl_panel3 = new GroupLayout(panel3);
		gl_panel3.setHorizontalGroup(
			gl_panel3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel3.createParallelGroup(Alignment.LEADING)
						.addComponent(checkAlwaysOnTop)
						.addComponent(checkPlayResponses))
					.addContainerGap(217, Short.MAX_VALUE))
		);
		gl_panel3.setVerticalGroup(
			gl_panel3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel3.createSequentialGroup()
					.addContainerGap()
					.addComponent(checkAlwaysOnTop)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkPlayResponses)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		panel3.setLayout(gl_panel3);
		
		GroupLayout gl_panel2 = new GroupLayout(panel2);
		gl_panel2.setHorizontalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel2.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMinPcm)
						.addComponent(lblSilenceTimeout))
					.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
					.addGroup(gl_panel2.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(spinnerMinPcm)
						.addComponent(spinnerSilenceTimeout))
					.addContainerGap())
		);
		gl_panel2.setVerticalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinPcm)
						.addComponent(spinnerMinPcm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSilenceTimeout)
						.addComponent(spinnerSilenceTimeout, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel2.setLayout(gl_panel2);
		
		
		GroupLayout gl_panel1 = new GroupLayout(panel1);
		gl_panel1.setHorizontalGroup(
			gl_panel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUserName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textUserName, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel1.setVerticalGroup(
			gl_panel1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName)
						.addComponent(textUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(35, Short.MAX_VALUE))
		);
		panel1.setLayout(gl_panel1);
		contentPane.setLayout(gl_contentPane);

		setVisible(true);
	}
	

	/**
	 * Se ejecuta presionando sobre el boton de Guardar.
	 * Actualiza la informacion de configuracion de Atenea (en memoria), re escribe el 
	 * archivo de configuracion y luego actualiza los valores de la interfaz principal.
	 */
	protected void saveButtonMouseClicked() {
		
		if (this.textUserName.getText().equals("")) {
			atenea.getConfiguration()
				.updateVariable("userName", "?");
		} else {
			atenea.getConfiguration()
				.updateVariable("userName", this.textUserName.getText());
		}
		
		atenea.getConfiguration()
			.updateVariable("silenceTimeout", this.spinnerSilenceTimeout.getValue().toString());
		
		atenea.getConfiguration()
			.updateVariable("minPcm", new BigDecimal(
					String.valueOf(this.spinnerMinPcm.getValue()))
					.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		atenea.getConfiguration()
			.updateVariable("alwaysOnTop", Boolean.toString(this.checkAlwaysOnTop.isSelected()));
		
		atenea.getConfiguration()
			.updateVariable("playResponses", Boolean.toString(this.checkPlayResponses.isSelected()));
		
		// Re-escribo el archivo de configuracion
		atenea.getConfiguration().updateConfigFile();
		
		// Actualizo los valores necesarios de la interfaz principal
		MainGUI.getInstance().setAlwaysOnTop(
				Boolean.parseBoolean(atenea.getConfiguration().getVariable("alwaysOnTop")));
		
		INSTANCE = null;
		dispose();
	}
	
	
	/**
	 * Se ejecuta presionando sobre el boton de Cancelar.
	 */
	protected void cancelButtonMouseClicked() {
		INSTANCE = null;
		dispose();
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de Restaurar valores por defecto.
	 */
	protected void defaultButtonMouseClicked() {
		textUserName.setText("");
		spinnerMinPcm.setValue(0.04f);
		spinnerSilenceTimeout.setValue(3);
		checkAlwaysOnTop.setSelected(true);
		checkPlayResponses.setSelected(true);
	}
	
}
