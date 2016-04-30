package test_planner;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;

public class TETSimple
{
    public static void main(String[] args)
    {
        final GazeManager gm = GazeManager.getInstance();
        boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);
        
        final GazeListener gazeListener = new GazeListener();
        gm.addGazeListener(gazeListener);
        
        //TODO: Do awesome gaze control wizardry
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                gm.removeGazeListener(gazeListener);
             gm.deactivate();
            }
        });
    }
    
    private static class GazeListener implements IGazeListener
    {
        @Override
        public void onGazeUpdate(GazeData gazeData)
        {
            System.out.println(gazeData.toString()+gazeData.rawCoordinates);
            
        }
    }
}