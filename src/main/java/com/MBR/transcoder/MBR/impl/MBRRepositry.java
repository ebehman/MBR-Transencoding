package com.MBR.transcoder.MBR.impl;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface MBRRepositry extends CassandraRepository<MBRPairing,Integer> {

}
