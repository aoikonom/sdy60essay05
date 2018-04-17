package streetmarker.aoikonom.sdy.streetmarker.data;

import java.util.List;

import streetmarker.aoikonom.sdy.streetmarker.model.Path;

/**
 * Created by aoiko on 11/4/2018.
 */

public interface IPathRetrieval {
    void onPathAdded(Path path,boolean newPath);
    void onPathCanceled();
}
