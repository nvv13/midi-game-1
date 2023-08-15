/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game1.midi.nvv.su;

import javax.sound.midi.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *
 * @author nvv
 * 
 
 private Button SustainPedal;  Сообщение Damper Pedal (оно же Sustain Pedal, оно же Hold Pedal), СС#64, 
  действует аналогично правой педали фортепиано (когда демпферы поднимаются со всех струн и остаются в таком 
  состоянии до отпускания педали). Ноты, звучащие в момент прихода сообщения, а также все последующие продолжают 
  звучать на участке поддержки (Sustain) до тех пор, пока педаль не будет отпущена. При этом неважно, отпустили вы 
  нажатые клавиши или нет. Если вы отпустили все клавиши, но держите педаль нажатой, ноты могут звучать 
  неограниченно долго. После отпускания педали все ноты на канале разом затухают (переходят в стадию Release).
   Если при нажатой педали пришло сообщение режима канала All Notes Off, то его выполнение задерживается до 
  отпускания педали.  Сообщение педали сустейна является настоящим пожирателем полифонии. Ведь пока нота не выключена,
  голос (или несколько голосов), необходимые для ее воспроизведения, заняты и не могут быть отданы под вновь
  поступившие ноты. Превысить доступную полифонию не составляет труда — достаточно при нажатой педали взять 
  по очереди десяток-другой нот. 
  Замечу, что это сообщение не позволяет осуществить полноценную имитацию действия правой педали фортепиано,
  поскольку имеет только два жестких состояния: On и Off. В реальной жизни правую педаль можно нажать не до конца,
   а чуть-чуть, чтобы демпферы оставались очень близко к струнам и в какой-то мере влияли на их звучание.  
  
 private Button SustenutoPedal; Сообщение Sustenuto (СС#66) действует аналогично средней педали 
  (sostenuto) у рояля (эта педаль еще называется селективной педалью сустейна). 
  При поступлении сообщения выдерживаются только ноты, которые в этот момент уже звучат. 
  Ноты, взятые при нажатой педали, ведут себя обычным образом. Так же, как и в предыдущем случае, 
  если при нажатой педали пришло сообщение режима канала All Notes Off, то его выполнение 
  задерживается до отпускания педали. 
  Опытные пианисты широко используют эту педаль, поскольку она позволяет придать исполнению тонкую нюансировку
   и избежать общего педального гула. Например, берется аккорд, затем нажимается педаль состенуто,
    затем аккорд снимается. Но он продолжает звучать, создавая общую гармонию,
     на фоне которой последующие ноты звучат с демпферами, а, значит, без гула, образуя интересные 
     сочетания с фоновым аккордом. 
 
 private Button SoftPedal;  Сообщение Soft Pedal (CC#67) действует аналогично левой педали фортепиано — 
   при ее нажатии звук приглушается. У пианино и роялей реализация механизма приглушения разная, 
   но в любом случае звук становится не только тише, но еще и приобретает более мягкую окраску. 
   Обычно тон-генераторы реагируют на это сообщение простым уменьшением громкости, но в некоторых пэтчах
    (имитирующих звуки рояля) может меняться и окраска.

 */
public class NewJFrame extends javax.swing.JFrame {
    
    public static final int END_OF_TRACK = 47;
    public static final int GAME_STAT_FIRST_NEW_NOTE = 0;
    public static final int GAME_STAT_WAIT_RIGHT_NOTE = 1;
    public static final int GAME_STAT_REPEAT_NOTE = 2;
    //Choice noteChoice = new Choice();
    static int velocity = 125; //64-volume is 50%  ,0-note off, 1-127-volume
  MidiDevice inDevice = null;
  MidiDevice outDevice = null;
    ArrayList<MidiDevice> inDevices = new ArrayList<>();
    ArrayList<MidiDevice> outDevices = new ArrayList<>();
  Sequence  sequence  = null;
  Sequencer sequencer = null;
  MyReceiver MyReceiver1 = null;
    //TimerTask task = new MyTask();
    //const int STEP_;
  Date date = new Date();
  int channel=0;
  int i_SustainPedal_value=0x0, i_SustenutoPedal_value=0x0, i_SoftPedal_value=0x0;
    Timer timer = new Timer();
  int iCheckNote=-1, iStep=GAME_STAT_FIRST_NEW_NOTE;
  int iNoteCount=0, iNoteRight=0, iNoteNoRight=0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton AllSoundOFF;
    private java.awt.Label LabelNoteCount;
    private java.awt.Label LabelNoteNoRight;
    private java.awt.Label LabelNoteRight;
    private java.awt.Label LabelSost;
    private javax.swing.JPanel PaneItog;
    private javax.swing.JToggleButton SoftPedal;
    private javax.swing.JToggleButton SustainPedal;
    private javax.swing.JToggleButton SustenutoPedal;
    private javax.swing.JButton btnFileChoice;
    private javax.swing.JButton btnOpenDev;
    private javax.swing.JButton btnPlayMidi;
    private javax.swing.JButton btnRefreshListDev;
    private javax.swing.JButton btnStartStopGame;
    private javax.swing.JCheckBox cbNoteA;
    private javax.swing.JCheckBox cbNoteA_sharp;
    private javax.swing.JCheckBox cbNoteB;
    private javax.swing.JCheckBox cbNoteC;
    private javax.swing.JCheckBox cbNoteC_sharp;
    private javax.swing.JCheckBox cbNoteD;
    private javax.swing.JCheckBox cbNoteD_sharp;
    private javax.swing.JCheckBox cbNoteE;
    private javax.swing.JCheckBox cbNoteF;
    private javax.swing.JCheckBox cbNoteF_sharp;
    private javax.swing.JCheckBox cbNoteG;
    private javax.swing.JCheckBox cbNoteG_sharp;
    private javax.swing.JCheckBox cbOctavaA;
    private javax.swing.JCheckBox cbOctavaA1;
    private javax.swing.JCheckBox cbOctavaA2;
    private javax.swing.JCheckBox cbOctava_a;
    private javax.swing.JCheckBox cbOctava_a1;
    private javax.swing.JCheckBox cbOctava_a2;
    private javax.swing.JCheckBox cbOctava_a3;
    private javax.swing.JCheckBox cbOctava_a4;
    private javax.swing.JCheckBox cbOctava_a5;
    private javax.swing.JTextField fileNameBox;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lbVelocity;
    private javax.swing.JComboBox midiInChoice;
    private javax.swing.JComboBox midiOutChoice;
    private javax.swing.JComboBox noteChoice;
    private javax.swing.JSlider slider_velocity;
    
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        
        initComponents();
        
        noteChoice.removeAllItems();
        noteChoice.addItem("Level2(1) D# E");
        noteChoice.addItem("Level2(2) E F");
        noteChoice.addItem("Level3 D# E F");
        noteChoice.addItem("Level4 D D# E F");
        noteChoice.addItem("Level5 D D# E F F#");
        noteChoice.addItem("Level6 C# D D# E F F#");
        noteChoice.addItem("Level7 C C# D D# E F F#");
        noteChoice.addItem("Level8 C C# D D# E F F# G");
        noteChoice.addItem("Level9 C C# D D# E F F# G G#");
        noteChoice.addItem("Level10 C C# D D# E F F# G G# A");
        noteChoice.addItem("Level11 C C# D D# E F F# G G# A A#");
        noteChoice.addItem("Level12 C C# D D# E F F# G G# A A# B");
        
        RefreshMidiDevLists();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

public void RefreshMidiDevLists()
{
try
 {
  midiOutChoice.removeAllItems();
  outDevices.clear();
  midiInChoice.removeAllItems();
  inDevices.clear();
  MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
     for (MidiDevice.Info info : infos) {
         MidiDevice device = MidiSystem.getMidiDevice(info);
         //System.out.print("\r\n");
         if (device.getMaxReceivers() > 0 || device.getMaxReceivers() == -1) {
             midiOutChoice.addItem(info.getName());// getDescription());
             outDevices.add(device);
         }
         if (device.getMaxTransmitters() > 0 || device.getMaxTransmitters() == -1) {
             midiInChoice.addItem(info.getName());//.getDescription());
             inDevices.add(device);
         }
     }
 } catch (MidiUnavailableException e) {System.out.print(e);}
}

   public void RefreshItog(){
	  LabelNoteCount.setText(String.format("Загадано нот=%d", iNoteCount));
	  LabelNoteRight.setText(String.format("Угадано=%d", iNoteRight));
	  LabelNoteNoRight.setText(String.format("Неверных попыток=%d", iNoteNoRight));
 }

  public int GetRandomNote() //
  {
	  /*private Checkbox cbNoteC , cbNoteC_sharp,
      cbNoteD , cbNoteD_sharp,
      cbNoteE ,
      cbNoteF , cbNoteF_sharp,
      cbNoteG , cbNoteG_sharp,
      cbNoteA , cbNoteA_sharp,
      cbNoteB ;*/
      ArrayList<Integer> NoteList = new ArrayList<>();
	  if(cbNoteC. isSelected())NoteList.add(0);
	  if(cbNoteC_sharp.isSelected())NoteList.add(1);
	  if(cbNoteD.isSelected())NoteList.add(2);
	  if(cbNoteD_sharp.isSelected())NoteList.add(3);
	  if(cbNoteE.isSelected())NoteList.add(4);
	  if(cbNoteF.isSelected())NoteList.add(5);
	  if(cbNoteF_sharp.isSelected())NoteList.add(6);
	  if(cbNoteG.isSelected())NoteList.add(7);
	  if(cbNoteG_sharp.isSelected())NoteList.add(8);
	  if(cbNoteA.isSelected())NoteList.add(9);
	  if(cbNoteA_sharp.isSelected())NoteList.add(10);
	  if(cbNoteB.isSelected())NoteList.add(11);
	  //
	  /*
	  private Checkbox cbOctavaA2, // А2-субконтроктава
      cbOctavaA1, // А1-контроктава
      cbOctavaA,  // А-большая октава
      cbOctava_a, // а-малая октава
      cbOctava_a1,// а1-первая октава
      cbOctava_a2,// а2
      cbOctava_a3,// а3
      cbOctava_a4,// а4
      cbOctava_a5;// а5
	   */
      ArrayList<Integer> OctavaList = new ArrayList<>();
	  if(cbOctavaA2.isSelected())OctavaList.add(0);
	  if(cbOctavaA1.isSelected())OctavaList.add(1);
	  if(cbOctavaA.isSelected())OctavaList.add(2);
	  if(cbOctava_a.isSelected())OctavaList.add(3);
	  if(cbOctava_a1.isSelected())OctavaList.add(4);
	  if(cbOctava_a2.isSelected())OctavaList.add(5);
	  if(cbOctava_a3.isSelected())OctavaList.add(6);
	  if(cbOctava_a4.isSelected())OctavaList.add(7);
	  if(cbOctava_a5.isSelected())OctavaList.add(8);
	  //
      if (NoteList.isEmpty() || OctavaList.isEmpty())
	   return -1;
	  //
	  Random random_note   = new Random();
	  int iNote=NoteList.get(random_note.nextInt(NoteList.size()));
      //
	  Random random_octave = new Random();
	  int iOctava=OctavaList.get(random_octave.nextInt(OctavaList.size())) + 1;
	  //
	  System.out.printf("Nota=%d, Octava=%d \r\n",iNote,iOctava);
	  return (12 * iOctava) +  iNote;
  }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnRefreshListDev = new javax.swing.JButton();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        midiInChoice = new javax.swing.JComboBox();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        midiOutChoice = new javax.swing.JComboBox();
        btnOpenDev = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnFileChoice = new javax.swing.JButton();
        fileNameBox = new javax.swing.JTextField();
        btnPlayMidi = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        SustainPedal = new javax.swing.JToggleButton();
        SustenutoPedal = new javax.swing.JToggleButton();
        SoftPedal = new javax.swing.JToggleButton();
        AllSoundOFF = new javax.swing.JToggleButton();
        slider_velocity = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        lbVelocity = new javax.swing.JLabel();
        javax.swing.JPanel panel_note = new javax.swing.JPanel();
        btnStartStopGame = new javax.swing.JButton();
        noteChoice = new javax.swing.JComboBox();
        cbNoteC = new javax.swing.JCheckBox();
        cbNoteC_sharp = new javax.swing.JCheckBox();
        cbNoteD = new javax.swing.JCheckBox();
        cbNoteD_sharp = new javax.swing.JCheckBox();
        cbNoteE = new javax.swing.JCheckBox();
        cbNoteF = new javax.swing.JCheckBox();
        cbNoteF_sharp = new javax.swing.JCheckBox();
        cbNoteG = new javax.swing.JCheckBox();
        cbNoteG_sharp = new javax.swing.JCheckBox();
        cbNoteA = new javax.swing.JCheckBox();
        cbNoteA_sharp = new javax.swing.JCheckBox();
        cbNoteB = new javax.swing.JCheckBox();
        javax.swing.JTextField jTextField1 = new javax.swing.JTextField();
        PaneItog = new javax.swing.JPanel();
        LabelSost = new java.awt.Label();
        LabelNoteCount = new java.awt.Label();
        LabelNoteRight = new java.awt.Label();
        LabelNoteNoRight = new java.awt.Label();
        jPanel6 = new javax.swing.JPanel();
        cbOctavaA2 = new javax.swing.JCheckBox();
        cbOctavaA1 = new javax.swing.JCheckBox();
        cbOctavaA = new javax.swing.JCheckBox();
        cbOctava_a = new javax.swing.JCheckBox();
        cbOctava_a1 = new javax.swing.JCheckBox();
        cbOctava_a2 = new javax.swing.JCheckBox();
        cbOctava_a3 = new javax.swing.JCheckBox();
        cbOctava_a4 = new javax.swing.JCheckBox();
        cbOctava_a5 = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MIDI Game demo");

        btnRefreshListDev.setText("refresh Dev");
        btnRefreshListDev.addActionListener(this::btnRefreshListDevActionPerformed);

        jLabel1.setText("MIDI IN");

        midiInChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("MIDI OUT");

        midiOutChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnOpenDev.setText("OpenDev");
        btnOpenDev.addActionListener(this::btnOpenDevActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefreshListDev)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midiInChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midiOutChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOpenDev)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, midiInChoice, midiOutChoice);
        
        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, btnOpenDev, btnRefreshListDev);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefreshListDev)
                    .addComponent(jLabel1)
                    .addComponent(midiInChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(midiOutChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenDev))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 153, 51));

        btnFileChoice.setText("file choice");
        btnFileChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFileChoiceActionPerformed(evt);
            }
        });

        fileNameBox.setText("http://www.jsbach.net/midi/bwv988/bwv988.mid");
        fileNameBox.setToolTipText("");

        btnPlayMidi.setText("play file");
        btnPlayMidi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayMidiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFileChoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, 690, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPlayMidi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFileChoice)
                    .addComponent(fileNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPlayMidi))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        SustainPedal.setText("Sustain");
        SustainPedal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SustainPedalActionPerformed(evt);
            }
        });

        SustenutoPedal.setText("Sustenuto");
        SustenutoPedal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SustenutoPedalActionPerformed(evt);
            }
        });

        SoftPedal.setText("Soft");
        SoftPedal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SoftPedalActionPerformed(evt);
            }
        });

        AllSoundOFF.setText("All Sound OFF (local control is set to off)");
        AllSoundOFF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllSoundOFFActionPerformed(evt);
            }
        });

        slider_velocity.setMajorTickSpacing(30);
        slider_velocity.setMaximum(127);
        slider_velocity.setPaintLabels(true);
        slider_velocity.setPaintTicks(true);
        slider_velocity.setValue(120);
        slider_velocity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slider_velocityStateChanged(evt);
            }
        });

        jLabel3.setText("0-note off, 1-127-volume");

        lbVelocity.setText("velocity            ");
        lbVelocity.setMaximumSize(new java.awt.Dimension(122, 14));
        lbVelocity.setMinimumSize(new java.awt.Dimension(122, 14));
        lbVelocity.setPreferredSize(new java.awt.Dimension(122, 14));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(SustainPedal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SustenutoPedal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SoftPedal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AllSoundOFF)
                .addGap(49, 49, 49)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbVelocity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(slider_velocity, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SustainPedal)
                            .addComponent(SustenutoPedal)
                            .addComponent(SoftPedal)
                            .addComponent(AllSoundOFF)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(slider_velocity, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbVelocity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        slider_velocity.getAccessibleContext().setAccessibleName("");
        slider_velocity.getAccessibleContext().setAccessibleDescription("");

        panel_note.setBackground(new java.awt.Color(255, 255, 255));

        btnStartStopGame.setText("Start");
        btnStartStopGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartStopGameActionPerformed(evt);
            }
        });

        noteChoice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        noteChoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteChoiceActionPerformed(evt);
            }
        });

        cbNoteC.setText("C");

        cbNoteC_sharp.setText("C#");

        cbNoteD.setText("D");

        cbNoteD_sharp.setText("D#");

        cbNoteE.setText("E");

        cbNoteF.setText("F");

        cbNoteF_sharp.setText("F#");

        cbNoteG.setText("G");

        cbNoteG_sharp.setText("G#");

        cbNoteA.setText("A");

        cbNoteA_sharp.setText("A#");

        cbNoteB.setText("B");

        jTextField1.setEditable(false);
        jTextField1.setText("до  до#  ре  ре#  ми  фа  фа#  соль  соль#  ля  ля#  си");

        javax.swing.GroupLayout panel_noteLayout = new javax.swing.GroupLayout(panel_note);
        panel_note.setLayout(panel_noteLayout);
        panel_noteLayout.setHorizontalGroup(
            panel_noteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_noteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnStartStopGame)
                .addGap(2, 2, 2)
                .addComponent(noteChoice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_noteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_noteLayout.createSequentialGroup()
                        .addComponent(cbNoteC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbNoteC_sharp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteD_sharp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbNoteF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteF_sharp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteG)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteG_sharp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbNoteA_sharp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbNoteB))
                    .addComponent(jTextField1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_noteLayout.setVerticalGroup(
            panel_noteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_noteLayout.createSequentialGroup()
                .addGroup(panel_noteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStartStopGame)
                    .addComponent(noteChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbNoteC)
                    .addComponent(cbNoteC_sharp)
                    .addComponent(cbNoteD)
                    .addComponent(cbNoteD_sharp)
                    .addComponent(cbNoteE)
                    .addComponent(cbNoteF)
                    .addComponent(cbNoteF_sharp)
                    .addComponent(cbNoteG)
                    .addComponent(cbNoteG_sharp)
                    .addComponent(cbNoteA)
                    .addComponent(cbNoteA_sharp)
                    .addComponent(cbNoteB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        PaneItog.setBackground(new java.awt.Color(204, 204, 255));

        LabelSost.setAlignment(java.awt.Label.CENTER);
        LabelSost.setMinimumSize(new java.awt.Dimension(200, 19));
        LabelSost.setName(""); // NOI18N
        LabelSost.setText("Выбирите источник");

        LabelNoteCount.setAlignment(java.awt.Label.CENTER);
        LabelNoteCount.setMinimumSize(new java.awt.Dimension(200, 19));
        LabelNoteCount.setText("Загадано нот");

        LabelNoteRight.setAlignment(java.awt.Label.CENTER);
        LabelNoteRight.setMinimumSize(new java.awt.Dimension(200, 19));
        LabelNoteRight.setText("Угадано");

        LabelNoteNoRight.setAlignment(java.awt.Label.CENTER);
        LabelNoteNoRight.setMinimumSize(new java.awt.Dimension(200, 19));
        LabelNoteNoRight.setName(""); // NOI18N
        LabelNoteNoRight.setText("Неверных попыток");

        javax.swing.GroupLayout PaneItogLayout = new javax.swing.GroupLayout(PaneItog);
        PaneItog.setLayout(PaneItogLayout);
        PaneItogLayout.setHorizontalGroup(
            PaneItogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaneItogLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(LabelSost, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelNoteCount, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelNoteRight, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelNoteNoRight, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PaneItogLayout.setVerticalGroup(
            PaneItogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaneItogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaneItogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelNoteCount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelSost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelNoteRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelNoteNoRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        cbOctavaA2.setText("А2-субконтроктава");

        cbOctavaA1.setText("А1-контроктава");

        cbOctavaA.setText("А-большая октава");

        cbOctava_a.setText("а-малая октава");

        cbOctava_a1.setSelected(true);
        cbOctava_a1.setText("а1-первая октава");

        cbOctava_a2.setText("а2");

        cbOctava_a3.setText("а3");

        cbOctava_a4.setText("а4");

        cbOctava_a5.setText("а5");

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("quit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_note, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PaneItog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbOctavaA2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbOctavaA1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbOctavaA)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbOctava_a)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOctava_a1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOctava_a2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbOctava_a3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOctava_a4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOctava_a5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_note, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbOctavaA2)
                    .addComponent(cbOctavaA1)
                    .addComponent(cbOctavaA)
                    .addComponent(cbOctava_a)
                    .addComponent(cbOctava_a1)
                    .addComponent(cbOctava_a2)
                    .addComponent(cbOctava_a3)
                    .addComponent(cbOctava_a4)
                    .addComponent(cbOctava_a5))
                .addGap(36, 36, 36)
                .addComponent(PaneItog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnOpenDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDevActionPerformed
  	try{
        if(btnOpenDev.getLabel()=="OpenDev")
  	 {
  	 btnOpenDev.setLabel("CloseDev");
  	 if(MyReceiver1!=null)
  	  {
  	  MyReceiver1.close();
  	  MyReceiver1=null;
  	  }
  	 if (inDevice != null)
  	  {
  	  inDevice.close();
  	  inDevice = null;
  	  }
         if (outDevice != null)
          {
          outDevice.close();
          outDevice = null;
          }
         inDevice = inDevices.get(midiInChoice.getSelectedIndex());
         outDevice = outDevices.get(midiOutChoice.getSelectedIndex());
    	 if (! inDevice.isOpen())inDevice.open();
         if (! outDevice.isOpen())outDevice.open();
        
        Transmitter t = inDevice.getTransmitter();
        if (t == null)System.err.println (inDevice + ".getTransmitter() == null");
        Receiver r = outDevice.getReceiver();
        if (r == null)System.err.println (outDevice + ".getReceiver() == null");
  		if (t != null && r != null)
  		 {
  		 MyReceiver1 = new MyReceiver(r);
         t.setReceiver (MyReceiver1);
         btnStartStopGame.enable();
     	 LabelSost.setText("Жду запуска игры");
  		 }
  		
        btnRefreshListDev.disable();
  		}
  		else
  		{
 	  	btnOpenDev.setLabel("OpenDev");
            if (MyReceiver1 != null)
 		 {
 		 MyReceiver1.close();
 		 MyReceiver1=null;
 		 }
 		if (inDevice != null)
 		 {
 		 inDevice.close();
 		 inDevice = null;
 		 }
        if (outDevice != null)
         {
           outDevice.close();
           outDevice = null;
         }
        btnRefreshListDev.enable();
 	    }
          		}
  	    catch (Exception ex)
  		        {
  	    ex.printStackTrace();
  	      }
      	 
    }//GEN-LAST:event_btnOpenDevActionPerformed

    private void btnFileChoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFileChoiceActionPerformed
       javax.swing.JFileChooser file1=new javax.swing.JFileChooser();      // TODO add your handling code here:
       javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
           "MIDI files", "midi");
       file1.setFileFilter(filter);
       int returnVal = file1.showOpenDialog(this);
       if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
        this.fileNameBox.setText(file1.getSelectedFile().getAbsolutePath());
       }

    }//GEN-LAST:event_btnFileChoiceActionPerformed

    private void btnRefreshListDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshListDevActionPerformed
        RefreshMidiDevLists();
    }//GEN-LAST:event_btnRefreshListDevActionPerformed

    private void btnPlayMidiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayMidiActionPerformed
         if (outDevice == null)
          {
              LabelSost.setText("Устройство MIDI не открыто!");
              return;
          }  		    //btnPlayMidi.disable();
  		    //fileNameBox.disable();
  		    //btnFileChoice.disable();
         	System.out.println("The Play Midi has been pressed ");
         	String fn = fileNameBox.getText();
         	if( fn !=null )
   			   System.out.println(fn);
	 		try {
	  		    if(sequencer==null)
	  		    {
	  		    	btnPlayMidi.setLabel("stop Play");
	            //Sequence sequence = MidiSystem.getSequence(new URL("http://www.polymusic.ru/basemf/djc0.mid"));
                    assert fn != null;
                    if (fn.toLowerCase().startsWith("http:"))
  	 		   sequence = MidiSystem.getSequence(new URL(fn));
  	        else
  	           sequence = MidiSystem.getSequence(new File(fn));
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.getTransmitter().setReceiver(MyReceiver1);
            sequencer.setSequence(sequence);
            double durationInSecs = sequencer.getMicrosecondLength() / 1000000.0;
            System.out.print(durationInSecs);

            // Let us know when it is done playing
            sequencer.addMetaEventListener(new MetaEventListener() {
              public void meta(MetaMessage m) {
                // A message of this type is automatically sent
                // when we reach the end of the track
                if (m.getType() == END_OF_TRACK)
                  {
          		  //btnPlayMidi.enable();
       		      //fileNameBox.enable();
      		      //btnFileChoice.enable();
                  //System.exit(0);
                  sequencer.close();
                  sequencer=null;
                  sequence=null;
                  btnPlayMidi.setLabel("Play Midi");
                  System.out.println("END_OF_TRACK");
                  }
                }
              });
            // Start playing
	        sequencer.start();
	  		    }
	  		    else
	  		    {
	  		    sequencer.stop();
	  		    //sequencer.setMicrosecondPosition(0);
                sequencer.close();
                sequencer=null;
                sequence=null;
                btnPlayMidi.setLabel("Play Midi");
                }
	 		} catch (IOException  e) {System.out.print(e);
	  	      } catch (InvalidMidiDataException e) {System.out.print(e);
	  	    } catch (MidiUnavailableException e) {System.out.print(e);
	  	      }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPlayMidiActionPerformed

    private void noteChoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteChoiceActionPerformed
        	// "Level1 D# F#"
    		String s_note=(String) noteChoice.getSelectedItem();// getItem(noteChoice.getSelectedIndex());
                if(s_note==null)s_note="";
        	//System.out.println(s_note);
    		cbNoteC.setSelected(false);
    		cbNoteC_sharp.setSelected(false);
    		cbNoteD.setSelected(false);
    		cbNoteD_sharp.setSelected(false);
    		cbNoteE.setSelected(false);
    		cbNoteF.setSelected(false);
    		cbNoteF_sharp.setSelected(false);
    		cbNoteG.setSelected(false);
    		cbNoteG_sharp.setSelected(false);
    		cbNoteA.setSelected(false);
    		cbNoteA_sharp.setSelected(false);
    		cbNoteB.setSelected(false);
        	String[] a_note=s_note.split(" ");
        for (String s : a_note) {
            //System.out.println("["+a_note[iInd]+"]");
            if (s.equalsIgnoreCase("C")) cbNoteC.setSelected(true);
            if (s.equalsIgnoreCase("C#")) cbNoteC_sharp.setSelected(true);
            if (s.equalsIgnoreCase("D")) cbNoteD.setSelected(true);
            if (s.equalsIgnoreCase("D#")) cbNoteD_sharp.setSelected(true);
            if (s.equalsIgnoreCase("E")) cbNoteE.setSelected(true);
            if (s.equalsIgnoreCase("F")) cbNoteF.setSelected(true);
            if (s.equalsIgnoreCase("F#")) cbNoteF_sharp.setSelected(true);
            if (s.equalsIgnoreCase("G")) cbNoteG.setSelected(true);
            if (s.equalsIgnoreCase("G#")) cbNoteG_sharp.setSelected(true);
            if (s.equalsIgnoreCase("A")) cbNoteA.setSelected(true);
            if (s.equalsIgnoreCase("A#")) cbNoteA_sharp.setSelected(true);
            if (s.equalsIgnoreCase("B")) cbNoteB.setSelected(true);
        }
        	
    }//GEN-LAST:event_noteChoiceActionPerformed

    private void SustainPedalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SustainPedalActionPerformed
	 		try {
	 		ShortMessage on_Sustain = new ShortMessage();
	 		if(i_SustainPedal_value==0)
	 		    {
	 			i_SustainPedal_value=0x7F;
	 		    SustainPedal.setBackground(Color.green);
	 		    }
	 		    else
	 		    {
	 		    i_SustainPedal_value=0x0;
	 		    SustainPedal.setBackground(Color.LIGHT_GRAY);
	 		    }
	 		on_Sustain.setMessage(0xB0, channel, 0x40, i_SustainPedal_value);
		    MyReceiver1.send(on_Sustain, date.getTime());
  	      } catch (InvalidMidiDataException e) {System.out.print(e);}
    }//GEN-LAST:event_SustainPedalActionPerformed

    private void AllSoundOFFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllSoundOFFActionPerformed
	 		try {
	 		ShortMessage on_AllSoundOFF = new ShortMessage();
	 		on_AllSoundOFF.setMessage(0xB0, channel, 0x78, 0);
		    MyReceiver1.send(on_AllSoundOFF, date.getTime());
	      } catch (InvalidMidiDataException e) {System.out.print(e);}
    }//GEN-LAST:event_AllSoundOFFActionPerformed

    private void SustenutoPedalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SustenutoPedalActionPerformed
	 		try {
	 		ShortMessage on_Sustenuto = new ShortMessage();
	 		if(i_SustenutoPedal_value==0)
 		     {
	 		 i_SustenutoPedal_value=0x7F;
	 		 SustenutoPedal.setBackground(Color.green);
 		     }
 		     else
 		     {
 		     i_SustenutoPedal_value=0x0;
 		     SustenutoPedal.setBackground(Color.LIGHT_GRAY);
 		     }
	 		on_Sustenuto.setMessage(0xB0, channel, 0x42, i_SustenutoPedal_value);
		    MyReceiver1.send(on_Sustenuto, date.getTime());
  	      } catch (InvalidMidiDataException e) {System.out.print(e);}
    }//GEN-LAST:event_SustenutoPedalActionPerformed

    private void SoftPedalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SoftPedalActionPerformed
	 		try {
	 		ShortMessage on_Soft = new ShortMessage();
	 		if(i_SoftPedal_value==0)
		     {
	 			i_SoftPedal_value=0x7F;
	 			SoftPedal.setBackground(Color.green);
		     }
		     else
		     {
		    	 i_SoftPedal_value=0x0;
		    	 SoftPedal.setBackground(Color.LIGHT_GRAY);
		     }
	 		on_Soft.setMessage(0xB0, channel, 0x43, i_SoftPedal_value);
		    MyReceiver1.send(on_Soft, date.getTime());
  	      } catch (InvalidMidiDataException e) {System.out.print(e);}
    }//GEN-LAST:event_SoftPedalActionPerformed

    private void btnStartStopGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartStopGameActionPerformed
  	   if(btnStartStopGame.getLabel()=="Start")
  		{
  		btnStartStopGame.setLabel("Stop");
  		iNoteCount=0; iNoteRight=0;	iNoteNoRight=0;
  		RefreshItog();
  		// Ждать 1 секунд, прежде чем выполнить task()...
                timer.schedule( new MyTask(), 1000 );
  		}
  		else
  		{
 	  	timer.purge();
  	  	btnStartStopGame.setLabel("Start");
                iCheckNote=-1; iStep=GAME_STAT_FIRST_NEW_NOTE;
                PaneItog.setBackground(Color.WHITE);
                LabelSost.setText("Жду запуска игры");
                }
    }//GEN-LAST:event_btnStartStopGameActionPerformed

    private void slider_velocityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slider_velocityStateChanged
        velocity=slider_velocity.getValue();
        lbVelocity.setText("velocity : " + velocity + " ");
    }//GEN-LAST:event_slider_velocityStateChanged
    
    public class MyReceiver implements Receiver {
        
        private final Receiver reciv1;
        
        public MyReceiver(Receiver receiver) {
            this.reciv1 = receiver;
        }
        
        // Method descriptor #8 (Ljavax/sound/midi/MidiMessage;J)V
        public void send(javax.sound.midi.MidiMessage arg0, long arg1) // void send(MidiMessage message,  long timeStamp);
        {
            byte[] m1 = arg0.getMessage();
            
            for (int iM = 0; iM < arg0.getLength(); iM++) {
                if (iM == 0)
                    System.out.printf("0x%x", m1[iM]);
                else
                    System.out.print(m1[iM]);
                System.out.print("\t");
            }
            System.out.println(arg1);
            
            
            if (iCheckNote != -1 && arg0.getLength() == 3 && m1[0] == (byte) ShortMessage.NOTE_ON) {
                System.out.print(arg0.getLength());
                System.out.print("\t");
                System.out.println(iStep);
                if (iStep == GAME_STAT_WAIT_RIGHT_NOTE) {
                    if (m1[1] == iCheckNote) {
                        PaneItog.setBackground(Color.GREEN);
                        LabelSost.setText("ОК, правильно!");
                        iNoteRight++;
                        iStep = GAME_STAT_FIRST_NEW_NOTE;
                        // Ждать 1 секунд, прежде чем выполнить task()...
                        //System.out.println(task);
                        timer.purge();
                        timer.schedule(new MyTask(), 1000);
                    } else {
                        PaneItog.setBackground(Color.RED);
                        LabelSost.setText("Не, правильно!, Попробуй ещё!");
                        iNoteNoRight++;
                        iStep = GAME_STAT_REPEAT_NOTE;
                        timer.purge();
                        timer.schedule(new MyTask(), 1000);
                        
                    }
                    RefreshItog();
                } else {
                    iStep = GAME_STAT_WAIT_RIGHT_NOTE;
                    //reciv1.send(arg0, arg1);
                }
                
            }
            //else
            reciv1.send(arg0, arg1);
            
        }
        
        // Method descriptor #1 ()V
        public void close() {
            reciv1.close();
        }
    }
    
    public class MyTask extends TimerTask {
        public void run() {
            System.out.println("Запуск задачи");
            try {
                if (outDevice != null && outDevice.isOpen()) {
                    if (iStep != GAME_STAT_FIRST_NEW_NOTE)
                        LabelSost.setText("Жду правильное нажатие ноты");
                    else {
                        iCheckNote = GetRandomNote();
                        iNoteCount++;
                        LabelSost.setText("Жду нажатие ноты");
                    }
                    PaneItog.setBackground(Color.YELLOW);
                    if (iCheckNote != -1) {
                        ShortMessage on = new ShortMessage();
                        on.setMessage(ShortMessage.NOTE_ON, channel, iCheckNote, velocity);
                        MyReceiver1.send(on, new Date().getTime());
                        
                        Thread.sleep(1000);
                        
                        ShortMessage off = new ShortMessage();
                        off.setMessage(ShortMessage.NOTE_OFF, channel, iCheckNote, velocity);
                        MyReceiver1.send(off, new Date().getTime());
                    }
                }
            } catch (InvalidMidiDataException | InterruptedException e) {
                System.out.print(e);
            }
        }
    }
    // End of variables declaration//GEN-END:variables
}
