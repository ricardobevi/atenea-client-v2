package org.squadra.atenea.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.stt.RecognizeTextThread;

/**
 * Interfaz de usuario principal del programa.
 * Esta estructurada con Java Swing.
 * Contiene las principales funciones (como dialogar por voz o texto, aprender palabra, etc.)
 * @author Leandro Morrone
 */
@SuppressWarnings("serial")
public class MainGUIPrototype extends JFrame {
	
	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea = Atenea.getInstance();
	
	/** Singleton */
	private static MainGUIPrototype INSTANCE = null;
	
	/**
	 * Crea una instancia de la interfaz, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static MainGUIPrototype createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainGUIPrototype();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de la interfaz de usuario
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static MainGUIPrototype getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: inicia la interfaz principal
	 * @author Leandro Morrone
	 */
	private MainGUIPrototype() {
		initComponents();
	}
	
	// Componentes de la interfaz de usuario
	private JTextField txtEstado;
	private JTextField txtTiempoDeRespuesta;
	private JTextField txtMetaData;
	private JLabel lblEstado;
	private JLabel lblEntradaAudio;
	private JTextArea txtEntradaAudio;
	private JLabel lblEntradaTexto;
	private JTextArea txtEntradaTexto;
	private JLabel lblSalida;
	private JTextArea txtSalida;
	private JLabel lblTiempoDeRespuesta;
	private JLabel lblMetaData;
	private JButton btnValorarBien;
	private JButton btnValorarMal;
	private JButton btnEnviar;
	private JButton btnGrabarDetener;
	private JButton btnReproducir;
	private JButton btnNuevoTermino;
	private JButton btnNuevaAccion;
	private JButton btnValorarRegular;
	
	/**
	 * Inicializo los componentes de la interfaz de usuario y la muestro en pantalla
	 * @author Leandro Morrone
	 */
	private void initComponents() {
		
		// Seteo el look de la interfaz
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Seteo las propiedades de la ventana
		this.setSize(new Dimension(486, 438));
		this.setTitle("Atenea (Interfaz de prueba)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		// Inicializo los componentes de la interfaz
		
		lblEntradaAudio = new JLabel("Entrada de audio:");
		
		txtEntradaAudio = new JTextArea();
		txtEntradaAudio.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtEntradaAudio.setBorder(UIManager.getBorder("TextField.border"));
		txtEntradaAudio.setEditable(false);
		txtEntradaAudio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnGrabarDetener = new JButton("Grabar / Detener");
		btnGrabarDetener.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnGrabarDetenerAction(e);
			}
		});
		
		btnReproducir = new JButton("Reproducir");
		btnReproducir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnReproducirAction(e);
			}
		});
		
		lblEstado = new JLabel("Estado del sistema:");
		
		txtEstado = new JTextField();
		txtEstado.setEditable(false);
		txtEstado.setColumns(10);
		setTxtEstadoDelSistema(atenea.getStateText());
		
		lblEntradaTexto = new JLabel("Entrada de texto:");
		
		txtEntradaTexto = new JTextArea();
		txtEntradaTexto.setBorder(UIManager.getBorder("TextField.border"));
		txtEntradaTexto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnEnviarAction(e);
			}
		});
		
		lblSalida = new JLabel("Salida:");
		
		txtSalida = new JTextArea();
		txtSalida.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtSalida.setBorder(UIManager.getBorder("TextField.border"));
		txtSalida.setEditable(false);
		txtSalida.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnValorarBien = new JButton("Ok");
		
		btnValorarMal = new JButton("Mal");
		
		lblTiempoDeRespuesta = new JLabel("Tiempo de respuesta:");
		
		txtTiempoDeRespuesta = new JTextField();
		txtTiempoDeRespuesta.setEditable(false);
		txtTiempoDeRespuesta.setColumns(10);
		
		lblMetaData = new JLabel("Meta-data:");
		
		txtMetaData = new JTextField();
		txtMetaData.setEditable(false);
		txtMetaData.setColumns(10);
		
		btnNuevoTermino = new JButton("Nuevo t\u00E9rmino");
		
		btnNuevaAccion = new JButton("Nueva acci\u00F3n");
		
		btnValorarRegular = new JButton("Reg");
		
		// Armo la estructura de la interfaz utilizando los componentes anteriores
		
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtEstado, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txtEntradaAudio, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnGrabarDetener, Alignment.TRAILING)
										.addComponent(btnReproducir, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))))
							.addGap(8))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblEstado)
							.addContainerGap(367, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblEntradaAudio)
							.addContainerGap(374, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEntradaTexto)
							.addContainerGap(374, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txtEntradaTexto, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txtSalida, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnValorarBien)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnValorarRegular)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnValorarMal))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnNuevoTermino)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNuevaAccion)))
							.addGap(8))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSalida)
							.addContainerGap(428, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTiempoDeRespuesta)
								.addComponent(txtTiempoDeRespuesta, 104, 104, 104))
							.addGap(27)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMetaData)
								.addComponent(txtMetaData, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
							.addGap(12))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEstado)
					.addGap(5)
					.addComponent(txtEstado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEntradaAudio)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnGrabarDetener, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnReproducir))
						.addComponent(txtEntradaAudio, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addComponent(lblEntradaTexto)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEntradaTexto, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSalida)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnValorarMal, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnValorarBien, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnValorarRegular, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtSalida, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTiempoDeRespuesta)
						.addComponent(lblMetaData))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtTiempoDeRespuesta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtMetaData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnNuevaAccion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNuevoTermino, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
					.addGap(19))
		);
		this.getContentPane().setLayout(groupLayout);
		
		btnNuevaAccion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
				ActionsGUIPrototype win = new ActionsGUIPrototype();
				win.launchFrame();
			}
		});
	}

	
	/**
	 * Si el sistema se encuentra en espera, comienza a grabar la entrada de audio.
	 * Si el sistema se encuentra grabando, detiene la grabacion y comienza el reconocimiento.
	 * Esta funcion se ejecuta cuando se presiona el boton de Grabar/Detener
	 * @param e Evento capturado al hacer click sobre el boton Grabar/Detener
	 * @author Leandro Morrone
	 */
	private void btnGrabarDetenerAction(MouseEvent e) {
		if (atenea.getState() == AteneaState.WAITING) {
			atenea.getMicrophone().startRecording();
		}
		else if (atenea.getState() == AteneaState.RECORDING) {
			atenea.getMicrophone().stopRecordingAndRecognize();
		}
	}
	
	/**
	 * Si el sistema se encuentra en espera, envia el mensaje para que sea reconocido.
	 * @param e Evento capturado al hacer click sobre el boton Enviar
	 */
	protected void btnEnviarAction(MouseEvent e) {
		if (atenea.getState() == AteneaState.WAITING) {
			atenea.setState(AteneaState.PROCESSING);
			new Thread(new RecognizeTextThread()).start();
		}
	}
	
	/**
	 * Si el sistema se encuentra en espera, envia el mensaje para que sea reconocido.
	 * @param e Evento capturado al hacer click sobre el boton Enviar
	 */
	protected void btnReproducirAction(MouseEvent e) {
		if (atenea.getState() == AteneaState.WAITING) {
			atenea.getMicrophone().playRecording();
		}
	}
	
	// Funciones para setear y obtener el contenido de los campos de texto de la interfaz
	
	public void setTxtEstadoDelSistema(String text) {
		txtEstado.setText(text + "...");
	}
	
	public void setTxtTiempoDeRespuesta(String text) {
		txtTiempoDeRespuesta.setText(text);
	}
	
	public void setTxtMetaData(String text) {
		txtMetaData.setText(text);
	}
	
	public void setTxtEntradaAudio(String text) {
		txtEntradaAudio.setText(text);
	}
	
	public void setTxtSalida(String text) {
		txtSalida.setText(text);
	}
	
	public String getTxtSalida() {
		return txtSalida.getText();
	}
	
	public String getTxtEntradaTexto() {
		return txtEntradaTexto.getText();
	}

}