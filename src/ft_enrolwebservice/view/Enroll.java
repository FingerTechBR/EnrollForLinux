/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ft_enrolwebservice.view;

import com.nitgen.SDK.BSP.NBioBSPJNI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;



/**
 *
 * @author Lincoln Berlick
 */
public class Enroll extends javax.swing.JFrame implements ActionListener {

    /**
     *  variáveis 
     */
      private NBioBSPJNI bsp;
      private NBioBSPJNI.FIR_HANDLE template;
      private NBioBSPJNI.INPUT_FIR  inputfirt;
      private NBioBSPJNI.FIR_HANDLE captura;
      private NBioBSPJNI.WINDOW_OPTION m_bspWindowOption;
      private NBioBSPJNI.FIR_HANDLE [][] m_CaptureFIRs;
      private NBioBSPJNI.FIR_HANDLE m_EnrollFIR;
      
    
    public Enroll() {        
       m_CaptureFIRs = new NBioBSPJNI.FIR_HANDLE[11][2];
       bsp = new NBioBSPJNI();      
       
        initComponents(); 
        if(CheckError()){
            return;
        } else{
              bsp.OpenDevice();
              if(!CheckError())
                  mudar_status("Dispositivo Inicializado com Sucesso");
        }
        
        
        
        
        //aqui adicionar os listeners aos botões
        minimo_dir.addActionListener(this);
        minimo_esque.addActionListener(this);
        anelar_esque.addActionListener(this);
        medio_esque.addActionListener(this);
        indicador_esque.addActionListener(this);
        polegador_esque.addActionListener(this);
        polegador_dir.addActionListener(this);
        indicador_dir.addActionListener(this);
        medio_dir.addActionListener(this);
        anelar_dir.addActionListener(this);
        
      
        
      
    }

    
    
    @Override
    public void actionPerformed(ActionEvent e) {
         JButton btn = (JButton) e.getSource();
        if(e.getSource() == minimo_esque ){     
            CapturaDedo(10,btn);                      
        } else if(e.getSource() == anelar_esque ){
           CapturaDedo(9,btn);                            
        } else if(e.getSource() == medio_esque ){ 
           CapturaDedo(8,btn); 
        } else if(e.getSource() == indicador_esque ){
           CapturaDedo(7,btn); 
        } else if(e.getSource() == polegador_esque ){
           CapturaDedo(6,btn); 
        } else if(e.getSource() == polegador_dir ){
           CapturaDedo(1,btn); 
        } else if(e.getSource() == indicador_dir ){      
           CapturaDedo(2,btn); 
        } else if(e.getSource() == medio_dir ){
           CapturaDedo(3,btn); 
        } else if(e.getSource() == anelar_dir ){
           CapturaDedo(4,btn); 
        } else if(e.getSource() == minimo_dir){
           CapturaDedo(5,btn);
            
        }
    }
    
    
    //Captura digital adicionar ao index 2 dedos em cada captura ** necessário para dar merge
    private void CapturaDedo(int dedo, JButton btn){
        for (int s = 0 ; s < 2 ; s++) {
            NBioBSPJNI.FIR_HANDLE hCaptureFIR  = bsp.new FIR_HANDLE();
            bsp.Capture(NBioBSPJNI.FIR_PURPOSE.VERIFY, hCaptureFIR, -1, null, m_bspWindowOption);
            if (!CheckError()){
                if (m_CaptureFIRs[dedo][s] != null) {
                    m_CaptureFIRs[dedo][s].dispose();
                    m_CaptureFIRs[dedo][s] = null;
                }
                m_CaptureFIRs[dedo][s] = hCaptureFIR;                
            }
         }  
        mudar_status("Capture success INDEX[ " + dedo + " ]");
                //btn.setBackground(Color.red);  
                Icon warnIcon = new ImageIcon("src\\ft_enrolwebservice\\imagens\\circleT.png");
                btn.setIcon(warnIcon);
     }
    
    //Converte TextEncode para FIR_HANDLE
    public NBioBSPJNI.FIR_TEXTENCODE FirToText(NBioBSPJNI.FIR_HANDLE fir){
        
        NBioBSPJNI.FIR_TEXTENCODE  text = bsp.new FIR_TEXTENCODE();
        bsp.GetTextFIRFromHandle(fir, text);
        return text;
        
    }
    
    //Convert input_FIR para TextEncode
    public NBioBSPJNI.INPUT_FIR TextToFir(NBioBSPJNI.FIR_TEXTENCODE fir){
        
        NBioBSPJNI.INPUT_FIR inputfir = bsp.new INPUT_FIR();         
        inputfir.SetTextFIR(fir);
        return inputfir;
       
         
        
    }
    
        //Converte FIR para Fir_Handle
    public NBioBSPJNI.FIR HandleToFir(NBioBSPJNI.FIR_HANDLE fir){

      NBioBSPJNI.FIR inputfir = bsp.new FIR();         
      bsp.GetFIRFromHandle(fir, inputfir);
      return inputfir;
       
         
        
    }
    
    //Convert InputFir para FIR
    public NBioBSPJNI.INPUT_FIR FirToInputFir(NBioBSPJNI.FIR fir){
        
     NBioBSPJNI.INPUT_FIR inputfir = bsp.new INPUT_FIR();         
     inputfir.SetFullFIR(fir);
     return inputfir;
       
         
        
    }
        
        
        //Verifica existência de erros no módulo BSP
        public Boolean CheckError() {
        if (bsp.IsErrorOccured())  {
            mudar_status("NBioBSP Error Occured [" + bsp.GetErrorCode() + "]");
            return true;
        }
        return false;
    }
        public void mudar_status(String text){
            jl_status.setText(text);
        }

        
        //Itinerar dedos
	private byte GetFingerCount() {
            byte nCount = 0;
            for (int i = 0 ; i < 10 ; i++) {
                if (m_CaptureFIRs[i][0] != null && m_CaptureFIRs[i][1] != null) {
                        nCount++;
                }
            }
            return nCount;
	}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        minimo_dir = new javax.swing.JButton();
        minimo_esque = new javax.swing.JButton();
        anelar_esque = new javax.swing.JButton();
        medio_esque = new javax.swing.JButton();
        indicador_esque = new javax.swing.JButton();
        polegador_esque = new javax.swing.JButton();
        polegador_dir = new javax.swing.JButton();
        indicador_dir = new javax.swing.JButton();
        medio_dir = new javax.swing.JButton();
        anelar_dir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jl_status = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(350, 400));
        setSize(new java.awt.Dimension(250, 250));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Captura"));
        jPanel2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel2.setMinimumSize(new java.awt.Dimension(276, 240));
        jPanel2.setPreferredSize(new java.awt.Dimension(522, 436));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        minimo_dir.setBackground(new java.awt.Color(255, 255, 255));
        minimo_dir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        minimo_dir.setBorder(null);
        jPanel2.add(minimo_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 18, 18));

        minimo_esque.setBackground(new java.awt.Color(255, 255, 255));
        minimo_esque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        minimo_esque.setBorder(null);
        jPanel2.add(minimo_esque, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 18, 18));

        anelar_esque.setBackground(new java.awt.Color(255, 255, 255));
        anelar_esque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        anelar_esque.setBorder(null);
        jPanel2.add(anelar_esque, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 18, 18));

        medio_esque.setBackground(new java.awt.Color(255, 255, 255));
        medio_esque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        medio_esque.setBorder(null);
        jPanel2.add(medio_esque, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 18, 18));

        indicador_esque.setBackground(new java.awt.Color(255, 255, 255));
        indicador_esque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        indicador_esque.setBorder(null);
        jPanel2.add(indicador_esque, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 18, 18));

        polegador_esque.setBackground(new java.awt.Color(255, 255, 255));
        polegador_esque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        polegador_esque.setBorder(null);
        jPanel2.add(polegador_esque, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 18, 18));

        polegador_dir.setBackground(new java.awt.Color(255, 255, 255));
        polegador_dir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        polegador_dir.setBorder(null);
        jPanel2.add(polegador_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 18, 18));

        indicador_dir.setBackground(new java.awt.Color(255, 255, 255));
        indicador_dir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        indicador_dir.setBorder(null);
        jPanel2.add(indicador_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 18, 18));

        medio_dir.setBackground(new java.awt.Color(255, 255, 255));
        medio_dir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        medio_dir.setBorder(null);
        jPanel2.add(medio_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 18, 18));

        anelar_dir.setBackground(new java.awt.Color(255, 255, 255));
        anelar_dir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/circleV.png"))); // NOI18N
        anelar_dir.setBorder(null);
        jPanel2.add(anelar_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 18, 18));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ft_enrolwebservice/imagens/mao.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, 180));

        jLabel1.setText("jLabel1");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 30));

        jl_status.setText("Status:");

        jButton1.setText("Finalizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jl_status))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton1)
                        .addGap(81, 81, 81)
                        .addComponent(jButton2)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jl_status)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        byte byfingerCount = GetFingerCount();
        byte byConvType = NBioBSPJNI.EXPORT_MINCONV_TYPE.FDU;        
        if (byfingerCount == 0) {
           mudar_status("Not Exist Capture Data");
           return;
        }
        
        
        NBioBSPJNI.Export exportEngine = bsp.new Export();
        NBioBSPJNI.Export.DATA exportData = exportEngine.new DATA();

        exportData.EncryptType = byConvType;
        exportData.SamplesPerFinger = 2;
        exportData.DefaultFingerID = 0;
        exportData.FingerNum = byfingerCount;
        exportData.FingerData = new NBioBSPJNI.Export.FINGER_DATA[byfingerCount];
        boolean bStop = false;
        int nFingerIndex = 0;
        NBioBSPJNI.INPUT_FIR inputFIR = bsp.new INPUT_FIR();        
        for (int nCaptureIndex = 0 ; nCaptureIndex < 10 ; nCaptureIndex++) {	
			
            if (m_CaptureFIRs[nCaptureIndex][0] != null && m_CaptureFIRs[nCaptureIndex][1] != null) {
                exportData.FingerData[nFingerIndex] = exportEngine.new FINGER_DATA();
                exportData.FingerData[nFingerIndex].FingerID = (byte)(nCaptureIndex + 1);
                exportData.FingerData[nFingerIndex].Template = new NBioBSPJNI.Export.TEMPLATE_DATA[2];
                for (int s = 0 ; s < 2 ; s++) {
                    inputFIR.SetFIRHandle(m_CaptureFIRs[nCaptureIndex][s]);
                    NBioBSPJNI.Export.DATA exportCaptureData = exportEngine.new DATA();					
                    exportEngine.ExportFIR(inputFIR, exportCaptureData, byConvType);

                    if (CheckError()) {
                            bStop = true;
                            break;
                    }
                exportData.FingerData[nFingerIndex].Template[s] = exportEngine.new TEMPLATE_DATA();
                exportData.FingerData[nFingerIndex].Template[s].Data = exportCaptureData.FingerData[0].Template[0].Data;                  
            }
            nFingerIndex++;
            }
            if (bStop) break;
        }
        if (m_EnrollFIR != null) {
            m_EnrollFIR.dispose();
            m_EnrollFIR = null;
        }
        m_EnrollFIR = bsp.new FIR_HANDLE();
        exportEngine.ImportFIR(exportData, m_EnrollFIR);
        if (!CheckError())
        {
            mudar_status("Template Criado Com Sucesso");

        }

        inputFIR = null;

	     
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
       if (m_EnrollFIR == null) {
			        mudar_status("Enroll FIR is null");
			return;
		}
		
		NBioBSPJNI.INPUT_FIR inputFIR = bsp.new INPUT_FIR();
		inputFIR.SetFIRHandle(m_EnrollFIR);
		
		Boolean bResult = new Boolean(false);
		bsp.Verify(inputFIR, bResult, null);
		
		if (!CheckError()) {
			if (bResult)
				mudar_status("verify OK");
			else
				mudar_status("verify failed");
		}
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Enroll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Enroll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Enroll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Enroll.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
       
        
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Enroll().setVisible(true);
            }
        });
        
        
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anelar_dir;
    private javax.swing.JButton anelar_esque;
    private javax.swing.JButton indicador_dir;
    private javax.swing.JButton indicador_esque;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jl_status;
    private javax.swing.JButton medio_dir;
    private javax.swing.JButton medio_esque;
    private javax.swing.JButton minimo_dir;
    private javax.swing.JButton minimo_esque;
    private javax.swing.JButton polegador_dir;
    private javax.swing.JButton polegador_esque;
    // End of variables declaration//GEN-END:variables
}
