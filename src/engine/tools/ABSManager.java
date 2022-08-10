package engine.tools;

import java.util.ArrayList;
import java.util.List;

public class ABSManager {

    private final List<SingleClientEntry> systemDataList;

    public ABSManager() {
        systemDataList = new ArrayList<>();
    }

  //  public synchronized void addChatString(String chatString, String username) {
    //    systemDataList.add(new SingleClientEntry(username, ));
   // }

    public synchronized List<SingleClientEntry> getChatEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > systemDataList.size()) {
            fromIndex = 0;
        }
        return systemDataList.subList(fromIndex, systemDataList.size());
    }

    public int getVersion() {
        return systemDataList.size();
    }

}
