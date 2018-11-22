package pbl2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginActivity implements ActionListener, KeyListener{
	//test id and pass
	private String cus = "cus";
	private String emp = "emp";
	private String adm = "adm";
	private String pass = "123";
	//
	private JFrame jf;
	private JLabel idTxt = new JLabel("ID");
	private JLabel passTxt = new JLabel("PW");
	private JTextField idInput = new JTextField();
	private JPasswordField passInput = new JPasswordField();
	private JButton loginBtn = new JButton("Login");
	
	
	public LoginActivity() {
		loginActivity();
	}
	
	private void loginActivity() {
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("Login");
		jf.setSize(300, 160);
		
		JPanel pan = new JPanel();
		pan.setLayout(null);
		
		idTxt.setBounds(20, 30, 60, 30);
		passTxt.setBounds(20, 70, 60, 30);
		idInput.setBounds(100, 30, 80, 30);
		passInput.setBounds(100, 70, 80, 30);
		loginBtn.setBounds(200, 45, 80, 40);
		
		idInput.setFocusTraversalKeysEnabled(false);
		passInput.setFocusTraversalKeysEnabled(false);
		idInput.addKeyListener(this);
		passInput.addKeyListener(this);
		
		pan.add(idTxt);
		pan.add(passTxt);
		pan.add(idInput);
		pan.add(passInput);
		pan.add(loginBtn);
		
		jf.add(pan);
		
		loginBtn.addActionListener(this);
	    
		
	    jf.setLocationRelativeTo(null);//make the frame as center
	    jf.setVisible(true);
	}

	//actionListener Interface
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == loginBtn) {
			String auth = "";
			
			if(cus.equals(idInput.getText()) && pass.equals(String.valueOf(passInput.getPassword()))) {
				auth = "cus";
			}else if(emp.equals(idInput.getText()) && pass.equals(String.valueOf(passInput.getPassword()))) {
				auth = "emp";
			}else if(adm.equals(idInput.getText()) && pass.equals(String.valueOf(passInput.getPassword()))) {
				auth = "adm";
			}
			new MainActivity(auth);
		}
	}
	//actionListener Interface END

	
	//KeyListener Interface
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(idInput.hasFocus() && e.getKeyChar() == KeyEvent.VK_TAB) {
			passInput.requestFocus();
		}else if(passInput.hasFocus() && e.getKeyChar() == KeyEvent.VK_TAB) {
			idInput.requestFocus();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			loginBtn.doClick();
		}
	}
	//KeyListener Interface END
	


}
