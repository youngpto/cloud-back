package com.iot.cloudback.service;

import com.iot.cloudback.dao.AlertDao;
import com.iot.cloudback.dao.MeasurementDao;
import com.iot.cloudback.dao.StatusDao;
import com.iot.cloudback.dao.WaypointDao;
import com.iot.cloudback.entity.Alert;
import com.iot.cloudback.entity.Measurement;
import com.iot.cloudback.entity.Status;
import com.iot.cloudback.entity.Waypoint;
import com.iot.cloudback.entity.base.UpdateStreamData;
import com.iot.cloudback.entity.constants.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.service
 * @ClassName: UpDataService
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 15:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 15:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service
public class UpDataService {

    @Autowired
    private AlertDao alertDao;

    @Autowired
    private MeasurementDao measurementDao;

    @Autowired
    private StatusDao statusDao;

    @Autowired
    private WaypointDao waypointDao;

    public void uploadData(String topic, int dataType, String value) throws IllegalArgumentException {
        switch (dataType) {
            case DataType.MEASUREMENT:
                measurementDao.add(new Measurement(topic, new Date(), value));
                break;
            case DataType.STATUS:
                statusDao.add(new Status(topic, new Date(), Integer.parseInt(value)));
                break;
            case DataType.WAYPOINT:
                String[] split = value.trim().split(",");
                String longitude = split[0];
                String latitude = split[1];
                String elevation = null;
                if (split.length == 3) {
                    elevation = split[2];
                }
                waypointDao.add(new Waypoint(topic, new Date(), longitude, latitude, elevation));
                break;
            case DataType.ALERT:
                alertDao.add(new Alert(topic, new Date(), value));
                break;
            default:
                throw new IllegalArgumentException("数据类型错误");
        }
    }

    public UpdateStreamData downloadOneByUdsId(int dataType, String upDateStreamId) {
        switch (dataType) {
            case DataType.MEASUREMENT:
                return measurementDao.findLatestOneByUdsId(upDateStreamId);
            case DataType.STATUS:
                return statusDao.findLatestOneByUdsId(upDateStreamId);
            case DataType.WAYPOINT:
                return waypointDao.findLatestOneByUdsId(upDateStreamId);
            case DataType.ALERT:
                return alertDao.findLatestOneByUdsId(upDateStreamId);
            default:
                throw new IllegalArgumentException("数据类型错误");
        }
    }

    public List<? extends UpdateStreamData> downloadListByUdsId(int dataType, String upDateStreamId, int skip, int limit) {
        switch (dataType) {
            case DataType.MEASUREMENT:
                return measurementDao.findListByUdsId(upDateStreamId, skip, limit);
            case DataType.STATUS:
                return statusDao.findListByUdsId(upDateStreamId, skip, limit);
            case DataType.WAYPOINT:
                return waypointDao.findListByUdsId(upDateStreamId, skip, limit);
            case DataType.ALERT:
                return alertDao.findListByUdsId(upDateStreamId, skip, limit);
            default:
                throw new IllegalArgumentException("数据类型错误");
        }
    }

    public List<? extends UpdateStreamData> downloadListByUdsIdWithPeriod(int dataType, String upDateStreamId, Date begin, Date end) {
        switch (dataType) {
            case DataType.MEASUREMENT:
                return measurementDao.findListByUdsIdWithPeriod(upDateStreamId, begin, end);
            case DataType.STATUS:
                return statusDao.findListByUdsIdWithPeriod(upDateStreamId, begin, end);
            case DataType.WAYPOINT:
                return waypointDao.findListByUdsIdWithPeriod(upDateStreamId, begin, end);
            case DataType.ALERT:
                return alertDao.findListByUdsIdWithPeriod(upDateStreamId, begin, end);
            default:
                throw new IllegalArgumentException("数据类型错误");
        }
    }

    public void delete(int dataType, String upDateStreamId, String key, String value) {
        switch (dataType) {
            case DataType.MEASUREMENT:
                measurementDao.delete(upDateStreamId, key, value);
                break;
            case DataType.STATUS:
                statusDao.delete(upDateStreamId, key, value);
                break;
            case DataType.WAYPOINT:
                waypointDao.delete(upDateStreamId, key, value);
                break;
            case DataType.ALERT:
                alertDao.delete(upDateStreamId, key, value);
                break;
            default:
                throw new IllegalArgumentException("数据类型错误");
        }
    }
}
