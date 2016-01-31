package org.assistments.service;

public class PrettyGoodService
{
  // Although "PGService" is a partner ref, we need to use a ref under which users have
  // been created. This service is not a partner directly, but rather acting on behalf
  // of a partner.
  //
  // So, we'll use a test partner that comes from TestHelper.PARTNER_REF in the SDK.
  // Not setting our to that constant since TestHelper is not included in the sdk jar.
  public final static String PARTNER_REF = "ASTest-Ref";
}
