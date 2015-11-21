package yolo.suaj.alertac;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by HP-LM on 31/10/2015.
 */
public class EnvioSMS {
    String telefono, contacto, mensaje;

    public EnvioSMS(String xtelefono, String xcontacto, String xmensaje){
        telefono=xtelefono;
        contacto=xcontacto;
        mensaje=xmensaje;
        envio();
    }

    public void envio(){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telefono, null,mensaje, null, null);
        Log.e("Mensaje", "se envio sms");
    }
}
