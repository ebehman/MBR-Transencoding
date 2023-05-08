package com.MBR.transcoder.MBR.impl;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MBRUtils
{
    private static final Logger logger = LoggerFactory.getLogger(MBRUtils.class);

    // To be used in DCO service
//    public static boolean pushmbrfiles2DCO(String Dloc,String Iloc,String protocol,String filebitrate,String fname)
//    {
//        String[] ooloc = gethostname(Dloc);
//        String hostname = ooloc[0];
//        String Oloc = ooloc[1];
////	url = "https://"+hostname+Oloc.replace(customer, )
//        Session session = SFTPConnectionMangr.getSftpSession(hostname);
//        ChannelSftp sftp = null;
//        logger.info("Session "+session);
//        boolean Ispushed = false;
//        String bitrate = filebitrate.replace(fname+"_", "");
//
//        try {
//            session.connect();
//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//            sftp = (ChannelSftp) channel;
//            int mode = ChannelSftp.OVERWRITE;
//            try
//            {
//                sftp.cd(Oloc);
//            }
//            catch(SftpException e)
//            {
//                logger.info(e.getMessage());
//                logger.info("Creating the output folder :"+Oloc);
//                if (!mkdir(sftp, Oloc));
//                {
//                    return Ispushed;
//                }
//
//
//            }
//
//            try
//            {
//                sftp.mkdir(fname);
//                logger.info(fname+" folder created");
//                sftp.cd(fname);
//            }
//            catch(Exception e)
//            {
//                sftp.cd(fname);
//                logger.info(fname+" folder already exists");
//
//
//            }
//
//            if(protocol.equalsIgnoreCase("HLS")|| protocol.equalsIgnoreCase("DASH"))
//            {
//                try {
//
//                    sftp.mkdir(bitrate);
//                    logger.info(bitrate+" folder created");
//                    sftp.cd(bitrate);
//                }
//
//                catch(Exception e )
//                {
//                    sftp.cd(bitrate);
//                    logger.info(bitrate+" folder exits");
//
//                }
//            }
//
//            if (protocol.equalsIgnoreCase("HLS"))
//            {
//                // Directory for HLS Files
//                try {
//                    sftp.mkdir("HLS");
//                    logger.info("HLS folder created");
//                    sftp.cd("HLS");
//                }
//                catch(Exception e)
//                {
//                    sftp.cd("HLS");
//                    logger.info("HLS folder Exists");
//
//                }
//
//            }
//		/* To be used in Future for DASH protocol
//		 *
//		if(protocol.equalsIgnoreCase("DASH"))
//		{
//			// Directory for DASH Files
//						try {
//						sftp.mkdir("DASH");
//						logger.info("DASH folder created");
//						sftp.cd("DASH");
//						}
//						catch(Exception e)
//						{
//							sftp.cd("DASH");
//							logger.info("DASH folder Exists");
//
//						}
//		}
//		**/
//            File idirectory = new File(Iloc);
//
//            for(File file :idirectory.listFiles())
//            {
//                if(file.isFile()&& protocol.equalsIgnoreCase("MP4"))
//                {
//                    String filename = file.getName();
//
//                    if (filename.contains(filebitrate))
//                    {	logger.info(filename);
//                        sftp.put(new FileInputStream(file),filename,mode);
//                        logger.info("SendFiletoDCO, file trasfergoing on " +filename);
//                        Files.delete(Paths.get(file.getAbsolutePath()));
//
//
//
//                    }
//                }
//                if(file.isFile()&& protocol.equalsIgnoreCase("HLS"))
//                {
//                    //Separating the HLS chunks folder that will contain all chunks and playlist of all bitrates
//
//                    String filename = file.getName();
//
//                    if(filename.contains(filebitrate)&&(filename.endsWith(".ts")||filename.endsWith(".m3u8")))
//                    {
//                        logger.info(filename);
//                        sftp.put(new FileInputStream(file),filename,mode);
//                        logger.info("SendFiletoDCO, file trasfergoing on " +filename);
//                        Files.delete(Paths.get(file.getAbsolutePath()));
//
//                    }
//                }
//
//
//            }
//
//            Ispushed = true;
//
//            return Ispushed;
//        }
//
//
//        catch (Exception e)
//        {
//
//            logger.error(e.getMessage());
//            logger.debug(e.getMessage(), e.fillInStackTrace());
//            return Ispushed;
//        }
//
//
//        finally
//        {
//            if (sftp!=null)
//            {
//                sftp.disconnect();
//            }
//            if(session!=null)
//            {
//                session.disconnect();
//            }
//
//            logger.info("session is closed ");
//
//        }
//
//    }
    private static String[] gethostname(String oloc) {

        String[] ooloc = oloc.replaceAll("sftp://", "").split("/", 2);


        // sftp://ftp.in.bitgravity.com/<company_name>/Dfolder).
        // TODO Auto-generated method stub
        return ooloc;
    }
    public static float gettransFilesSize(String iloc,String filename)
    {
        File idirectory = new File(iloc);
        float filesize =0;
        for(File file :idirectory.listFiles())
        {
            String filen = file.getName();
            if(filen.contains(filename)&&filen.endsWith(".ts"))
            {
                filesize = filesize+file.length();


            }
        }

        filesize = filesize / (1024 * 1024);

        return filesize;

    }
/* To be used in case of DCO.
    public static boolean pushMasterplaylist(String Dloc,String Iloc, String fname)
    {
        String[] ooloc = gethostname(Dloc);
        String hostname = ooloc[0];
        String Oloc = ooloc[1];
//	url = "https://"+hostname+Oloc.replace(customer, )
        Session session = SFTPConnectionMangr.getSftpSession(hostname);
        ChannelSftp sftp = null;
        logger.info("Session "+session);
        boolean Ispushed = false;

        try {
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            int mode = ChannelSftp.OVERWRITE;
            sftp.cd(Oloc);
            sftp.cd(fname);

            File file = new File(Iloc+"/master.m3u8");
            sftp.put(new FileInputStream(file),file.getName(),mode);
            logger.info("SendFiletoDCO, file trasfered " +file.getName());
            Files.delete(Paths.get(file.getAbsolutePath()));
            Ispushed = true;
            return Ispushed;
        }
        catch (Exception e )
        {

            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e.fillInStackTrace());
            return Ispushed;
        }



        finally
        {
            if (sftp!=null)
            {
                sftp.disconnect();
                sftp.exit();
            }
            if(session!=null)
            {
                session.disconnect();

            }

            logger.info("session is closed ");

        }
    }

    public static boolean mkdir(ChannelSftp sftp, String oloc)
    {
        String f[] = oloc.split("/");
        boolean Iscreate = false;
        for (String o :f)
        {
            try {
                sftp.mkdir(o);
                sftp.cd(o);

            }

            catch(SftpException e)
            {
                logger.info(o+" folder exists");
                try {
                    sftp.cd(o);

                } catch (SftpException e1) {
                    // TODO Auto-generated catch block
                    logger.info(e.getLocalizedMessage());
                    return Iscreate;
                }

            }


        }
        Iscreate = true;
        return Iscreate;
    }

 */
}
