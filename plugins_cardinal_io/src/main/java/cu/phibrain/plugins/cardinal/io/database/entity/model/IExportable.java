package cu.phibrain.plugins.cardinal.io.database.entity.model;

import java.util.Date;

public interface IExportable extends IEntity {
    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getUpdatedAt();

    void setUpdatedAt(Date updated);

    Boolean getIsSync();

    void setIsSync(Boolean isSync);

    void setSyncDate(Date SyncDate);

    Date getSyncDate();

    Boolean mustExport();

    IExportable toRemoteObject();

    Long getRemoteId();

    void setRemoteId(Long remoteId);

    void setDeleted(Boolean deleted);

    Boolean getDeleted();
}
