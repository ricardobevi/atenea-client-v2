package org.squadra.atenea.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import lombok.extern.log4j.Log4j;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.actions.Executer;
import org.squadra.atenea.actions.ListOfAction;
import org.squadra.atenea.actions.MouseEventHandler;

/**
 * Interfaz de usuario utilizada para grabar macros.
 * Esta estructurada con Java Swing.
 * @author Leandro Morrone
 * @author Lucas Paradisi
 *
 */
@Log4j
@SuppressWarnings("serial")
public class ActionsGUI extends JFrame {

	/** Singleton */
	private static ActionsGUI INSTANCE = null;

	/** Manejador de eventos del mouse */
	private MouseEventHandler mouseHandler;

	/** Ejecutador de acciones */
	private Executer executer = new Executer();

	/** Variable que indica si se esta grabando */
	private boolean isRecording;

	/**
	 * Crea una instancia de la interfaz, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static ActionsGUI createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ActionsGUI();
		}
		return INSTANCE;
	}

	/**
	 * Devuelve la instancia de la interfaz de usuario
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static ActionsGUI getInstance() {
		return INSTANCE;
	}

	/**
	 * Constructor privado: inicia la interfaz principal
	 * @author Leandro Morrone
	 */
	private ActionsGUI() {
		initComponents();
	}

	// Puntos utilizados para el drag de la interfaz
	private Point startDrag;
	private Point startLoc;

	// Elementos swing de la interfaz de usuario
	private JLabel lblBackground;
	private JLabel lblRecordButton;
	private JLabel lblPlayButton;
	private JLabel lblHelpButton;
	private JLabel lblCloseButton;
	private JLabel lblState;
	private JTextField txtActionName;
	private JComboBox<String> comboActionType;

	/**
	 * Inicializo los componentes de la interfaz de usuario y la muestro en pantalla
	 * @author Leandro Morrone
	 */
	private void initComponents() {

		//=================== PROPIEDADES DE LA VENTANA =====================

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Atenea - "); //TODO: poner el AteneaState en el titulo
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		setSize(379, 100);
		setBackground(new Color(0,0,0,0));
		setIconImage(Resources.Images.ateneaIcon);
		setLocationRelativeTo(null);
		setResizable(false);
		setAlwaysOnTop(true); //TODO: leer de archivo config

		//========================== BACKGROUND ============================= 

		lblBackground = new JLabel();
		lblBackground.setIcon(Resources.Images.Backgrounds.actions);
		lblBackground.setBounds(0, 0, 
				lblBackground.getIcon().getIconWidth(), 
				lblBackground.getIcon().getIconHeight());

		lblBackground.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				backgroundMousePressed(evt);
			}
		});
		lblBackground.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				backgroundMouseDragged(evt);
			}
		});

		//====================== BOTON PARA CERRAR ==========================

		lblCloseButton = new JLabel();
		lblCloseButton.setIcon(Resources.Images.CloseButton.grey);
		lblCloseButton.setToolTipText("Cerrar");
		lblCloseButton.setBounds(336, 57, 
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
				lblCloseButton.setIcon(Resources.Images.CloseButton.blue);
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				lblCloseButton.setIcon(Resources.Images.CloseButton.grey);
			}
		});

		//======================== BOTON DE AYUDA ===========================

		lblHelpButton = new JLabel();
		lblHelpButton.setIcon(Resources.Images.HelpButton.grey);
		lblHelpButton.setToolTipText("Ayuda");
		lblHelpButton.setBounds(304, 57, 
				lblHelpButton.getIcon().getIconWidth(), 
				lblHelpButton.getIcon().getIconHeight());

		lblHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					helpButtonMouseClicked();
				}
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblHelpButton.setIcon(Resources.Images.HelpButton.blue);
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				lblHelpButton.setIcon(Resources.Images.HelpButton.grey);
			}
		});

		//============== BOTON PARA GRABAR ACCION / DETENER =================

		lblRecordButton = new JLabel();
		lblRecordButton.setIcon(Resources.Images.RecordButton.grey);
		lblRecordButton.setToolTipText("Iniciar grabación");
		lblRecordButton.setBounds(20, 57, 
				lblRecordButton.getIcon().getIconWidth(), 
				lblRecordButton.getIcon().getIconHeight());

		lblRecordButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					recordButtonMouseClicked();
				}
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				if (isRecording) {
					lblRecordButton.setIcon(Resources.Images.StopButton.blue);
				} else {
					lblRecordButton.setIcon(Resources.Images.RecordButton.red);
				}
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				if (isRecording) {
					lblRecordButton.setIcon(Resources.Images.StopButton.grey);
				} else {
					lblRecordButton.setIcon(Resources.Images.RecordButton.grey);
				}
			}
		});

		//================= BOTON PARA REPRODUCIR ACCION ====================

		lblPlayButton = new JLabel();
		lblPlayButton.setIcon(Resources.Images.PlayButton.grey);
		lblPlayButton.setToolTipText("Reproducir acción grabada");
		lblPlayButton.setBounds(93, 57, 
				lblPlayButton.getIcon().getIconWidth(), 
				lblPlayButton.getIcon().getIconHeight());

		lblPlayButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					playButtonMouseClicked();
				}
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblPlayButton.setIcon(Resources.Images.PlayButton.blue);
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				lblPlayButton.setIcon(Resources.Images.PlayButton.grey);
			}
		});

		//===================== TEXTAREA DE ENTRADA =========================

		final String defaultActionName = "Nombre de la acción";
		txtActionName = new JTextField(defaultActionName);
		txtActionName.setFont(new Font("Arial", Font.PLAIN, 12));
		txtActionName.setForeground(Color.GRAY);
		txtActionName.setBorder(BorderFactory.createCompoundBorder(
				new LineBorder(new Color(175, 175, 175), 4), 
				BorderFactory.createEmptyBorder(3, 5, 3, 5)));

		txtActionName.setBounds(20, 18, 220, 31);
		txtActionName.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				txtAreaLight(txtActionName, true);
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				txtAreaLight(txtActionName, false);
			}
		});
		txtActionName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (txtActionName.getText().equals(defaultActionName)) {
					txtActionName.setText("");
					txtActionName.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				if (txtActionName.getText().equals("")) {
					txtActionName.setText(defaultActionName);
					txtActionName.setForeground(Color.GRAY);
				}
			}
		});

		//================ COMBO CON LOS TIPOS DE ACCIONES ==================

		comboActionType = new JComboBox<String>();
		comboActionType.addItem("Click");
		comboActionType.addItem("Doble click");
		comboActionType.addItem("Click derecho");
		comboActionType.setBackground(Color.WHITE);
		comboActionType.setBorder(new LineBorder(new Color(175, 175, 175), 3));
		comboActionType.setBounds(248, 18, 112, 31);

		//====================== TEXTO CON EL ESTADO ========================

		lblState = new JLabel("");
		lblState.setBounds(130, 58, 150, 25);


		//===================================================================
		// Agrego todos los elementos a la interfaz en diferentes capas

		JLayeredPane layeredPane = getLayeredPane();
		layeredPane.add(lblBackground, new Integer(1));   //el Integer es el z-index

		layeredPane.add(lblCloseButton, new Integer(2));
		layeredPane.add(lblHelpButton, new Integer(2));
		layeredPane.add(lblRecordButton, new Integer(2));
		layeredPane.add(lblPlayButton, new Integer(2));
		layeredPane.add(lblState, new Integer(2));

		layeredPane.add(txtActionName, new Integer(3));
		layeredPane.add(comboActionType, new Integer(3));

		// Hago visible la GUI una vez que termino de cargar todos los componentes
		setVisible(true);

	}


	/**
	 * Se ejecuta presionando sobre el boton de cerrar.
	 * Cierra el programa
	 */
	protected void closeButtonMouseClicked() {
		if (isRecording) {
			stopActionRecord();
		}
		ListOfAction.getInstance().writeToFile();
		INSTANCE = null;
		Atenea.getInstance().setState(AteneaState.WAITING);
		MainGUI.getInstance().maximizeButtonMouseClicked();
		dispose();
	}

	/**
	 * Se ejecuta presionando sobre el boton de ayuda.
	 * Abre la seccion de ayuda del sitio web.
	 */
	protected void helpButtonMouseClicked() {
		// TODO Auto-generated method stub
	}

	/**
	 * Se ejecuta presionando sobre el boton de grabar/detener.
	 * Si esta en estado de espera, inicio la grabacion de la macro.
	 * Si esta en estado grabando, detiene la grabacion de la macro.
	 */
	protected void recordButtonMouseClicked() {

		// Si esta grabando una accion -> detiene la grabacion
		if (isRecording)
		{
			stopActionRecord();
			lblRecordButton.setIcon(Resources.Images.RecordButton.red);
			lblRecordButton.setToolTipText("Iniciar grabación");
			lblState.setText("Fin de la grabación");
		}
		// Si no esta grabando -> comienza la grabacion
		else
		{
			// Si la accion no tiene nombre
			if (txtActionName.getText().isEmpty())
			{
				lblState.setText("Falta el nombre de la acción");
			}
			else
			{
				initActionRecord();
				lblRecordButton.setIcon(Resources.Images.StopButton.blue);
				lblRecordButton.setToolTipText("Detener grabación");
				lblState.setText("<html><body>Grabando...<br>Mantenga CTRL y seleccione</body></html>");
			}
		}
	}

	/**
	 * Inicia la grabacion de macros
	 */
	private void initActionRecord() {
		try {
			Thread.sleep(500);
			//setExtendedState(JFrame.ICONIFIED);

			GlobalScreen.registerNativeHook();
			mouseHandler = new MouseEventHandler(txtActionName.getText(), (String) comboActionType.getSelectedItem()) ;

		} catch (NativeHookException ex) {
			log.error("There was a problem registering the native hook.");
			log.error(ex.getMessage());
			System.exit(1);
		} catch (Exception e1) {
		}

		// Inicio el proceso de captura de clicks
		GlobalScreen.getInstance().addNativeMouseListener(mouseHandler);
		GlobalScreen.getInstance().addNativeMouseMotionListener(mouseHandler);
		GlobalScreen.getInstance().addNativeKeyListener(mouseHandler);

		isRecording = true;
		log.debug("Inicio de captura");
	}

	/**
	 * Detiene la grabacion de macros
	 */
	private void stopActionRecord() {
		mouseHandler.finish();

		// Termino el proceso de captura de clicks
		GlobalScreen.getInstance().removeNativeMouseListener(mouseHandler);
		GlobalScreen.getInstance().removeNativeMouseMotionListener(mouseHandler);
		GlobalScreen.getInstance().removeNativeKeyListener(mouseHandler);
		GlobalScreen.unregisterNativeHook();

		isRecording = false;
		log.debug("Fin de captura");
	}

	/**
	 * Se ejecuta presionando sobre el boton de reproducir.
	 * Reproduce la ultima accion grabada.
	 */
	protected void playButtonMouseClicked() {
		try {
			Thread.sleep(500);
			this.setExtendedState(JFrame.ICONIFIED);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		System.out.println("Reproduciendo...");
		// Saco los espacios introducidos en el textbox
		String names = txtActionName.getText().replaceAll("\\ ", "");
		String[] files = null;

		// Separo las acciones con '+'
		// Ej: Word + word_pegar
		if (names.contains("+")) {
			files = names.split("\\+");
		} else {
			files = new String[1];
			files[0] = names;
		}
		executer.execute(files);
	}

	/**
	 * Se ejecuta cuando se hace click sobre el fondo de la interfaz.
	 * Guarda la posicion donde se hizo click.
	 * @param evt Evento capturado del mouse.
	 */
	protected void backgroundMousePressed(MouseEvent evt) {
		this.startDrag = this.getScreenLocation(evt);
		this.startLoc = this.getLocation();
	}

	/**
	 * Se ejecuta cuando se hace un arrastre de la interfaz.
	 * Mueve la interfaz a medida que se arrastra el mouse.
	 * @param evt Evento capturado del mouse.
	 */
	protected void backgroundMouseDragged(MouseEvent evt) {
		Point current = this.getScreenLocation(evt);
		Point offset = new Point((int) current.getX() - (int) startDrag.getX(),
				(int) current.getY() - (int) startDrag.getY());

		Point new_location = new Point(
				(int) (this.startLoc.getX() + offset.getX()), (int) (this.startLoc
						.getY() + offset.getY()));
		this.setLocation(new_location);
	}

	private Point getScreenLocation(MouseEvent e) {
		Point cursor = e.getPoint();
		Point target_location = this.getLocationOnScreen();
		return new Point((int) (target_location.getX() + cursor.getX()),
				(int) (target_location.getY() + cursor.getY()));
	}

	private void txtAreaLight(JTextField area, boolean light) {
		if (light) {
			area.setBorder(BorderFactory.createCompoundBorder(
					new LineBorder(new Color(190, 190, 190), 4), 
					BorderFactory.createEmptyBorder(3, 5, 3, 5)));
		}
		else {
			area.setBorder(BorderFactory.createCompoundBorder(
					new LineBorder(new Color(175, 175, 175), 4), 
					BorderFactory.createEmptyBorder(3, 5, 3, 5)));
		}
	}

	/** 
	 * @brief setea el texto del TextField de ActionGUI
	 * @param name
	 */
	public void setActionText(String name)
	{
		txtActionName.setForeground(Color.BLACK);
		txtActionName.setText(name);
	}

}
