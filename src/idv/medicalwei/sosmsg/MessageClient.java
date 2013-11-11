package idv.medicalwei.sosmsg;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class MessageClient extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private String tableName = DBHelper.tableName;
    private SQLiteDatabase db;
    PeerListListener myPeerListListener;

    public MessageClient(WifiP2pManager manager, Channel channel, MainActivity activity) {
    	super();
        this.mManager = manager;
        this.mChannel = channel;
        this.db = new DBHelper(activity).getWritableDatabase();
    }

	@Override
	public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // TODO: hint to enable wifi p2p
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (mManager != null) {
                mManager.requestPeers(mChannel, new PeerListListener() {
                	@Override
                	public void onPeersAvailable(WifiP2pDeviceList peers) {
                		Collection<WifiP2pDevice> deviceList = peers.getDeviceList();
                		Iterator<WifiP2pDevice> iter= deviceList.iterator();
                		while(true){
                			WifiP2pDevice device = iter.next();
                			WifiP2pConfig config = new WifiP2pConfig();
                			config.deviceAddress = device.deviceAddress;
                			mManager.connect(mChannel, config, new MessageActionListener(device.deviceAddress));
                		}
                	}
                });
            }
        }
	}
	
	private void sendMessage(String deviceAddress){
		Cursor cursor = db.query(tableName, null, "hop<30", null, null, null, null);
		cursor.moveToFirst();
        Socket socket = new Socket();
		try {
		    socket.bind(null);
		    socket.connect((new InetSocketAddress(deviceAddress, 9999)), 500);
			ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
			os.writeObject(Integer.valueOf(cursor.getCount()));
			while(cursor.moveToNext()){
				SOSMessage sosMessage = new SOSMessage(cursor);
				os.writeObject(sosMessage);
			}
			
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public class MessageActionListener implements ActionListener{
		String deviceAddress;
	
		public MessageActionListener(String devaddr){
			super();
			this.deviceAddress = devaddr;
		}
		
		@Override
		public void onSuccess() {
			Log.d("MessageReceiver", "Connected to " + this.deviceAddress);
			sendMessage(this.deviceAddress);
		}
		
		@Override
		public void onFailure(int reason) {
			// failure logic
		}
	}
}
