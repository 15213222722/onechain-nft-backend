DROP TABLE IF EXISTS `achievement_definitions`;
CREATE TABLE `achievement_definitions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NULL COMMENT '成就类型',
  `category` VARCHAR(255) NULL COMMENT '分类,collector:收藏家;trader:交易;creator:铸造;social:社交;special:特殊',
  `nameEn` VARCHAR(255) NULL COMMENT '成就英文名称',
  `nameZh` VARCHAR(255) NULL COMMENT '成就中文名称',
  `descriptionEn` VARCHAR(255) NULL COMMENT '成就英文描述',
  `descriptionZh` VARCHAR(255) NULL COMMENT '成就中文描述',
  `icon` VARCHAR(255) NULL COMMENT '徽章标识',
  `badgeColor` VARCHAR(255) NULL COMMENT '徽章颜色/样式',
  `tierThresholds` VARCHAR(255) NULL COMMENT '段位阈值（JSON 格式）：{ bronze: 1, silver: 10, gold: 50, platinum: 100, diamond: 500 }',
  `pointsPerTier` VARCHAR(255) NULL COMMENT '每个等级奖励的积分/经验值',
  `isActive` TINYINT(1) NULL COMMENT '是否生效',
  `displayOrder` INT NULL COMMENT '显示顺序',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就定义表';

DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NULL COMMENT '活动类型,mint:铸造;list:上架;sale:出售;transfer:转移;offer:报价;like:喜欢;follow:关注',
  `userId` INT NULL COMMENT '用户id',
  `targetUserId` INT NULL COMMENT '对方用户id',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `contractAddress` VARCHAR(255) NULL COMMENT '合约地址',
  `nftName` VARCHAR(255) NULL COMMENT 'nft名称',
  `nftImage` VARCHAR(255) NULL COMMENT 'nft图片',
  `price` DECIMAL(20, 9) NULL COMMENT 'nft价格',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `metadata` VARCHAR(255) NULL COMMENT '额外信息',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动跟踪表';

DROP TABLE IF EXISTS `collection_stats`;
CREATE TABLE `collection_stats` (
  `collectionSlug` VARCHAR(255) NOT NULL,
  `period` VARCHAR(255) NOT NULL COMMENT '统计周期：24h=过去24小时，7d=过去7天，30d=过去30天，all=累计全部时间',
  `trade_volume_raw` BIGINT NULL COMMENT '该周期内该集合的总成交金额（仅 Sale 类型，以原始数值计，单位为代币最小单位）',
  `trade_count` INT NULL COMMENT '该周期内该集合的总成交笔数（Sale + Swap 均计入）',
  `unique_traders` INT NULL COMMENT '该周期内参与该集合交易的独立用户数（DISTINCT taker 和 lister）',
  `floor_price_raw` BIGINT NULL COMMENT '当前最低活跃 SALE 挂单价格（原始数值，需根据 coin decimals 换算展示）',
  `last_floor_update` DATETIME NULL COMMENT '最后一次更新地板价的时间（用于判断 floor_price_raw 是否新鲜）',
  `sale_count` INT NULL COMMENT '该周期内 Sale 类型（固定价购买）的成交笔数',
  `swap_count` INT NULL COMMENT '该周期内 Swap 类型（NFT 交换）的成交笔数',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='NFT 集合统计汇总表（用于集合排行榜、地板价展示、交易量趋势等）';

DROP TABLE IF EXISTS `collections`;
CREATE TABLE `collections` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL COMMENT '用户id',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

DROP TABLE IF EXISTS `contract_whitelist`;
CREATE TABLE `contract_whitelist` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `packageId` VARCHAR(255) NULL COMMENT '合约',
  `name` VARCHAR(255) NULL COMMENT '白名单名称',
  `isActive` TINYINT(1) NULL COMMENT '是否启用',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `websiteUrl` VARCHAR(255) NULL COMMENT '网站',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合约白名单表';

DROP TABLE IF EXISTS `conversations`;
CREATE TABLE `conversations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `participant1Id` INT NULL COMMENT '参与者1',
  `participant2Id` INT NULL COMMENT '参与者2',
  `lastMessageId` INT NULL COMMENT '上一条消息id',
  `lastMessagePreview` VARCHAR(255) NULL COMMENT '最后一条消息预览',
  `unreadCount1` INT NULL COMMENT '未读条数1',
  `unreadCount2` INT NULL COMMENT '未读条数2',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话表';

DROP TABLE IF EXISTS `fee_config_updates`;
CREATE TABLE `fee_config_updates` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `fee_config_object_id` VARCHAR(255) NULL COMMENT '手续费配置对象的 Sui Object ID (0x...，对应 FeeConfig 的 id)',
  `old_fee_bps` INT NULL COMMENT '变更前的费率（basis points，例如 100 = 1%）',
  `new_fee_bps` INT NULL COMMENT '变更后的费率（basis points，例如 250 = 2.5%）',
  `old_recipient` VARCHAR(255) NULL COMMENT '变更前的手续费接收地址',
  `new_recipient` VARCHAR(255) NULL COMMENT '变更后的手续费接收地址',
  `tx_digest` VARCHAR(255) NULL COMMENT '执行费率变更的 Sui 交易 Digest（交易哈希/标识符）',
  `timestamp_ms` BIGINT NULL COMMENT '费率变更发生的链上时间戳（毫秒，来自 tx_context::epoch_timestamp_ms）',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='手续费配置变更记录（FeeUpdated 事件对应的历史记录）';

DROP TABLE IF EXISTS `kiosk_create_event`;
CREATE TABLE `kiosk_create_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `walletAddress` VARCHAR(255) NULL COMMENT '钱包地址',
  `kioskId` VARCHAR(255) NULL COMMENT '市场id',
  `capId` VARCHAR(255) NULL COMMENT 'capId',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `eventType` VARCHAR(255) NULL COMMENT '事件type',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='kiosk创建';

DROP TABLE IF EXISTS `listings`;
CREATE TABLE `listings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `listing_object_id` VARCHAR(255) NULL COMMENT 'OneChain Object ID (0x...)',
  `owner_address` VARCHAR(255) NULL COMMENT '挂单人地址',
  `nft_object_id` VARCHAR(255) NULL COMMENT '挂单中的 NFT Object ID',
  `nft_type` VARCHAR(255) NULL COMMENT 'NFT 类型全名 (type_name::with_original_ids<T>())',
  `listing_type` INT NULL COMMENT '0=SWAP, 1=SALE',
  `status` INT NULL COMMENT '0=ACTIVE, 1=FILLED, 2=CANCELLED',
  `expected_nft_type` VARCHAR(255) NULL COMMENT 'Swap 时期望的 NFT 类型',
  `price` BIGINT NULL COMMENT 'SALE 时的价格（原始数值，需注意 decimals）',
  `coin_type` VARCHAR(255) NULL COMMENT 'SALE 时的支付币种类型',
  `collection_slug` VARCHAR(255) NULL COMMENT 'NFT 集合标识（链下映射或从元数据获取）',
  `collection_name` VARCHAR(255) NULL COMMENT '集合名称（可选，便于展示）',
  `create_tx_digest` VARCHAR(255) NULL COMMENT '创建交易 digest',
  `filled_tx_digest` VARCHAR(255) NULL COMMENT '挂单被成交 digest',
  `filled_timestamp_ms` BIGINT NULL COMMENT '挂单被成交时的链上时间戳',
  `cancel_tx_digest` VARCHAR(255) NULL COMMENT '挂单被取消 digest',
  `cancel_timestamp_ms` BIGINT NULL COMMENT '挂单被取消时的链上时间戳',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='NFT 挂单表';

DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `senderUserId` INT NULL COMMENT '发送方id',
  `receiverUserId` INT NULL COMMENT '接收方id',
  `messageType` VARCHAR(255) NULL COMMENT '消息类型,text:普通文本;offer:报价;counter_offer:',
  `content` TEXT NULL COMMENT '消息内容',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft 对象id',
  `offerAmount` DECIMAL(20, 9) NULL COMMENT '报价金额',
  `isRead` TINYINT(1) NULL COMMENT '是否已读',
  `deletedBySender` TINYINT(1) NULL COMMENT '被发送方删除',
  `deletedByReceiver` TINYINT(1) NULL COMMENT '被接收方删除',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

DROP TABLE IF EXISTS `nft_bids`;
CREATE TABLE `nft_bids` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `bidderUserId` INT NULL COMMENT '竞价用户id',
  `amount` VARCHAR(255) NULL COMMENT '金额',
  `isWinning` TINYINT(1) NULL COMMENT '是否胜出',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞价记录跟踪表';

DROP TABLE IF EXISTS `nft_delist_event`;
CREATE TABLE `nft_delist_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `walletAddress` VARCHAR(255) NULL COMMENT '钱包地址',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `kioskId` VARCHAR(255) NULL COMMENT '市场id',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `eventType` VARCHAR(255) NULL COMMENT '事件type',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下架事件表';

DROP TABLE IF EXISTS `nft_likes`;
CREATE TABLE `nft_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL COMMENT '用户id',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='喜欢表';

DROP TABLE IF EXISTS `nft_listing_event`;
CREATE TABLE `nft_listing_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `walletAddress` VARCHAR(255) NULL COMMENT '钱包地址',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `kioskId` VARCHAR(255) NULL COMMENT '市场id',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `eventType` VARCHAR(255) NULL COMMENT '事件type',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  `listingPrice` DECIMAL(20, 9) NULL COMMENT '上架价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上架事件表';

DROP TABLE IF EXISTS `nft_offers`;
CREATE TABLE `nft_offers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `offererUserId` INT NULL COMMENT '报价用户id',
  `amount` DECIMAL(20, 9) NULL COMMENT '金额',
  `status` VARCHAR(255) NULL COMMENT '状态,pending:待处理;accepted:已接受;rejected:已拒绝;expired:已过期;cancelled:已取消',
  `expiresAt` DATETIME NULL COMMENT '过期时间',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报价单';

DROP TABLE IF EXISTS `nft_placed_event`;
CREATE TABLE `nft_placed_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `walletAddress` VARCHAR(255) NULL COMMENT '钱包地址',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `kioskId` VARCHAR(255) NULL COMMENT '市场id',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `eventType` VARCHAR(255) NULL COMMENT '事件type',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='nft放入市场事件';

DROP TABLE IF EXISTS `nft_taken_event`;
CREATE TABLE `nft_taken_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `walletAddress` VARCHAR(255) NULL COMMENT '钱包地址',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `kioskId` VARCHAR(255) NULL COMMENT '市场id',
  `txHash` VARCHAR(255) NULL COMMENT 'hash',
  `eventType` VARCHAR(255) NULL COMMENT '事件type',
  `createdAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='nft取走事件';

DROP TABLE IF EXISTS `nft_transactions`;
CREATE TABLE `nft_transactions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nftObjectId` VARCHAR(255) NULL COMMENT '对象id',
  `type` VARCHAR(255) NULL COMMENT '交易类型,mint:铸造;list:上架;unlist:下架;transfer:转移;auction_won:竞拍',
  `fromUserId` INT NULL COMMENT '交易用户id',
  `toUserId` INT NULL COMMENT '对方用户id',
  `price` DECIMAL(20, 9) NULL COMMENT '成交价格',
  `txHash` VARCHAR(255) NULL COMMENT '交易hash',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='nft交易跟踪表';

DROP TABLE IF EXISTS `nfts`;
CREATE TABLE `nfts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `objectId` VARCHAR(255) NULL COMMENT '对象id',
  `contractAddress` VARCHAR(255) NULL COMMENT '合约地址',
  `name` VARCHAR(255) NULL COMMENT '名称',
  `description` TEXT NULL COMMENT '描述',
  `imageUrl` VARCHAR(255) NULL COMMENT '图片地址',
  `kioskId` VARCHAR(255) NULL COMMENT '拥有者',
  `attributes` VARCHAR(255) NULL COMMENT '属性',
  `rarityType` VARCHAR(255) NULL COMMENT '稀有度，common：普通；rare：稀有；epic：史诗；legendary：传说',
  `rarityRank` INT NULL COMMENT '稀有度排名',
  `isListed` TINYINT(1) NULL COMMENT '是否上架',
  `listingPrice` DECIMAL(20, 9) NULL COMMENT '上架价格',
  `category` VARCHAR(255) NULL COMMENT '分类：art:数字艺术；pfp：PFP/头像；assets：游戏资产；collectibles：收藏品；utility：实用型；media：音视频；others：其他',
  `auctionStartPrice` DECIMAL(20, 9) NULL COMMENT '竞拍开始价格',
  `auctionEndPrice` DECIMAL(20, 9) NULL COMMENT '竞拍最新价格',
  `auctionStartTime` DATETIME NULL COMMENT '竞拍开始时间',
  `auctionEndTime` DATETIME NULL COMMENT '竞拍结束时间',
  `mintTxHash` VARCHAR(255) NULL COMMENT '铸造hash',
  `viewCount` INT NULL COMMENT '浏览次数',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  `seriesId` INT NULL COMMENT '所属系列',
  `creatorAddress` VARCHAR(255) NULL COMMENT '创建者',
  `ownerAddress` VARCHAR(255) NULL COMMENT '拥有者',
  `nftType` VARCHAR(255) NULL COMMENT 'NFT 类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='nft表';

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL COMMENT '用户id',
  `type` VARCHAR(255) NULL COMMENT '通知类型,bid_received:有人出价竞拍你的 NFT;bid_outbid:你被超越出价;auction_ending:拍卖即将结束;auction_won:你赢得了拍卖;auction_sold:你的拍卖已售出;offer_received:有人出价;offer_received:你的出价已被接受;sale_complete:你的 NFT 已售出;follow_new:有人关注了你;price_drop:关注的 NFT 价格下降;system:系统通知',
  `title` VARCHAR(255) NULL COMMENT '标题',
  `message` TEXT NULL COMMENT '消息内容',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `actionUrl` VARCHAR(255) NULL COMMENT '关联跳转地址',
  `isRead` TINYINT(1) NULL COMMENT '是否已读',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

DROP TABLE IF EXISTS `platform_stats_daily`;
CREATE TABLE `platform_stats_daily` (
  `statDate` VARCHAR(255) NOT NULL COMMENT '统计日期 (YYYY-MM-DD)',
  `trade_volume_raw` BIGINT NULL COMMENT '所有 Sale payment_amount 之和',
  `trade_count` INT NULL COMMENT '累计交易',
  `unique_buyers` INT NULL COMMENT 'DISTINCT taker',
  `unique_sellers` INT NULL COMMENT 'DISTINCT lister',
  `fee_earned_raw` BIGINT NULL COMMENT '累计手续费',
  `swap_count` INT NULL COMMENT '累计swap单数',
  `sale_count` INT NULL COMMENT '累计sale单数',
  `active_listings` INT NULL COMMENT '当前活跃挂单数 (需定时更新)',
  `created_listings` INT NULL COMMENT '当天新增挂单',
  `cancelled_listings` INT NULL COMMENT '当天取消挂单',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`statDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台每日统计快照';

DROP TABLE IF EXISTS `price_alerts`;
CREATE TABLE `price_alerts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL COMMENT '用户id',
  `nftObjectId` VARCHAR(255) NULL COMMENT 'nft对象id',
  `alertType` VARCHAR(255) NULL COMMENT '提醒类型,below:低于（价格下跌至低于预警范围）;above:高于（价格上涨至高于预警范围）',
  `targetPrice` DECIMAL(20, 9) NULL COMMENT '目标价格',
  `priceWhenSet` DECIMAL(20, 9) NULL COMMENT '设置提醒时的当前价格',
  `isActive` TINYINT(1) NULL COMMENT '是否生效',
  `isTriggered` TINYINT(1) NULL COMMENT '是否触发',
  `triggeredAt` DATETIME NULL COMMENT '触发时间',
  `triggeredPrice` DECIMAL(20, 9) NULL COMMENT '触发时价格',
  `notificationSent` TINYINT(1) NULL COMMENT '通知是否已发送',
  `notes` VARCHAR(255) NULL COMMENT '提醒内容',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='价格提醒表';

DROP TABLE IF EXISTS `processed_events`;
CREATE TABLE `processed_events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tx_hash` VARCHAR(255) NULL COMMENT 'hash',
  `event_type` VARCHAR(255) NULL COMMENT '事件类型',
  `processed_at` DATETIME NULL,
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='已处理事件表';

DROP TABLE IF EXISTS `series`;
CREATE TABLE `series` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL COMMENT '系列key',
  `zh_desc` VARCHAR(255) NULL COMMENT '中文名称',
  `en_desc` VARCHAR(255) NULL COMMENT '英文名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系列表';

DROP TABLE IF EXISTS `trades`;
CREATE TABLE `trades` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `listing_object_id` VARCHAR(255) NULL,
  `trade_type` VARCHAR(255) NULL COMMENT '1=Sale (买NFT), 2=Swap (NFT↔NFT)',
  `taker_address` VARCHAR(255) NULL COMMENT '主动成交方 (买家/提供换入NFT的人)',
  `lister_address` VARCHAR(255) NULL COMMENT '挂单人 (卖家/原NFT持有者)',
  `nft_object_id_out` VARCHAR(255) NULL COMMENT '给 taker 的 NFT (原挂单的)',
  `nft_object_id_in` VARCHAR(255) NULL COMMENT '给 lister 的 NFT (Swap 时才有)',
  `payment_amount` BIGINT NULL COMMENT 'Sale 时的支付金额 (原始值)',
  `fee_amount` BIGINT NULL COMMENT 'Sale 时的手续费',
  `seller_amount` BIGINT NULL COMMENT 'Sale 时代理收到金额',
  `coin_type` VARCHAR(255) NULL COMMENT 'Sale 时的支付币种类型',
  `collection_slug` VARCHAR(255) NULL COMMENT '集合标识 (方便聚合)',
  `tx_digest` VARCHAR(255) NULL COMMENT '成交交易 digest',
  `timestamp_ms` BIGINT NULL COMMENT '成交时间戳 (ms)',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='NFT 成交记录表';

DROP TABLE IF EXISTS `user_achievements`;
CREATE TABLE `user_achievements` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NULL COMMENT '用户id',
  `achievementType` VARCHAR(255) NULL COMMENT '成就类型',
  `tier` VARCHAR(255) NULL COMMENT '成就等级;bronze:青铜;silver:白银;gold:黄金;platinum:铂金;diamond:钻石',
  `progress` INT NULL COMMENT '当前进度',
  `target` INT NULL COMMENT '目标进度',
  `isCompleted` TINYINT(1) NULL COMMENT '是否完成成就',
  `completedAt` DATETIME NULL COMMENT '完成时间',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户成就表';

DROP TABLE IF EXISTS `user_activity`;
CREATE TABLE `user_activity` (
  `address` VARCHAR(255) NOT NULL COMMENT '用户钱包地址（Sui 地址，0x...，作为主键）',
  `total_trades` INT NULL COMMENT '该用户累计参与的成交次数（Sale + Swap 均计入）',
  `total_volume_raw` BIGINT NULL COMMENT '该用户累计成交金额总和（仅 Sale 类型，以原始数值计，单位为代币最小单位）',
  `buy_volume_raw` BIGINT NULL COMMENT '该用户作为买家（taker）的累计成交金额（Sale 类型，支付金额）',
  `sell_volume_raw` BIGINT NULL COMMENT '该用户作为卖家（lister）的累计成交金额（Sale 类型，收到金额，不含手续费）',
  `fee_paid_raw` BIGINT NULL COMMENT '该用户累计支付的手续费总额（Sale 类型中作为买家支付的部分）',
  `last_active` DATETIME NULL COMMENT '该用户最后一次参与交易（成交）的时间（后端记录，基于链上 timestamp_ms）',
  `rank_volume` INT NULL COMMENT '按 total_volume_raw 降序排名的名次（可通过定时任务计算并更新）',
  `rank_trades` INT NULL COMMENT '按 total_trades 降序排名的名次（可通过定时任务计算并更新）',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户交易活动汇总表（用于用户排行榜、个人交易面板、鲸鱼榜等）';

DROP TABLE IF EXISTS `user_follows`;
CREATE TABLE `user_follows` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `followerUserId` INT NULL COMMENT '用户id',
  `followingId` INT NULL COMMENT '被关注用户id',
  `createdAt` DATETIME NULL,
  `updatedAt` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL COMMENT '用户名',
  `wallet_address` VARCHAR(255) NULL COMMENT '钱包地址',
  `email` VARCHAR(255) NULL COMMENT 'email',
  `last_signed_in` DATETIME NULL COMMENT '上次登录时间',
  `avatar_url` VARCHAR(255) NULL COMMENT '头像',
  `description` TEXT NULL COMMENT '个人简介',
  `twitter` VARCHAR(255) NULL COMMENT 'twitter',
  `created_at` DATETIME NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

