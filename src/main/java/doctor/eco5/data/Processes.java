package doctor.eco5.data;

import doctor.eco5.types.DataAction;
import doctor.eco5.types.DataBlock;

import java.sql.SQLException;

public class Processes {
    public static boolean init(DataBlock dataBlock) throws SQLException {
        return DataAdapter.dataAction(dataBlock, DataAction.initialization);
    }
    public static boolean arch(DataBlock dataBlock) throws SQLException {
        return DataAdapter.dataAction(dataBlock, DataAction.archivization);
    }
}